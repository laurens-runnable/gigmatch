import { randomUUID } from 'crypto'
import { H3Event } from 'h3'
import { VacancyDocument } from '~/../shared/mongo'
import { OpenVacancy } from '~/server/avro/commands'
import { createMatchServiceClient } from '~/server/lib/axios'
import { getDb } from '~/server/lib/mongo'
import { useJwt } from '~/server/lib/session'

export interface ExperienceInput {
  skillId: string
  experienceLevel: string
}

export interface CreateVacancyInput {
  jobTitle: string
  start: Date
  end: Date
  experience: Array<ExperienceInput>
  rateAmount: number
  rateType: string
  deadline: Date
}

export async function createVacancy(
  input: CreateVacancyInput,
  event: H3Event
): Promise<VacancyDocument | null> {
  const id = randomUUID()
  const { sub: userId } = await useJwt(event)

  const doc: VacancyDocument = { id, userId, isOpen: false, ...input }
  const db = await getDb()
  const coll = db.collection<VacancyDocument>('vacancy')
  await coll.insertOne(doc)

  return doc
}

export async function openVacancy(id: string, event: H3Event): Promise<void> {
  const db = await getDb()
  const coll = db.collection<VacancyDocument>('vacancy')
  const doc = await coll.findOne({ id })
  if (doc === undefined) {
    throw new Error(`Vacancy not found: ${id}`)
  }

  const client = await createMatchServiceClient(event)
  const buffer = OpenVacancy.toBuffer(doc)
  await client.post('/api/v1/commands', buffer, {
    headers: {
      'X-gm.type': OpenVacancy.name,
    },
  })
}

export interface VacancyFilter {
  id: [string]
}

export async function queryVacancies(
  filter: VacancyFilter,
  event: H3Event
): Promise<VacancyDocument[]> {
  const { sub: userId } = await useJwt(event)

  const db = await getDb()
  const coll = db.collection<VacancyDocument>('vacancy')
  const documents = coll
    .find({ id: { $in: filter.id } }, userId)
    .sort({ name: 1 })

  return documents.toArray()
}
