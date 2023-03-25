import { randomUUID } from 'crypto'
import { H3Event } from 'h3'
import { VacancyDocument } from '~/../shared/mongo'
import { OpenVacancy } from '~/server/avro/commands'

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

  const doc: VacancyDocument = {
    ...input,
    id,
    status: 'PENDING',
    userId,
  }
  const db = await useDb()
  const coll = db.collection<VacancyDocument>('vacancy')
  await coll.insertOne(doc)

  return doc
}

export async function openVacancy(id: string, event: H3Event): Promise<void> {
  const db = await useDb()
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

export interface VacancyFilterInput {
  id?: string[]
  type?: string
}

async function createFilter(input: VacancyFilterInput, event: H3Event): any {
  const { sub: userId } = await useJwt(event)

  const filter: any = { userId }

  const ids = input.id ?? []
  if (ids.length > 0) {
    filter.id = { $in: ids }
  }

  switch (input.type) {
    case 'ACTIVE':
      filter.status = { $in: ['OPEN', 'PENDING'] }
      break
    case 'CLOSED':
      filter.status = 'CLOSED'
      break
  }

  return filter
}

export async function queryVacancies(
  input: VacancyFilterInput,
  event: H3Event
): Promise<VacancyDocument[]> {
  const db = await useDb()
  const coll = db.collection<VacancyDocument>('vacancy')
  const filter = await createFilter(input, event)
  const documents = coll.find(filter).sort({ name: 1 })

  return documents.toArray()
}
