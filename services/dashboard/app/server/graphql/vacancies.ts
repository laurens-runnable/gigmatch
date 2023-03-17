import { randomUUID } from 'crypto'
import { H3Event } from 'h3'
import { OpenVacancy } from '~/server/avro/commands'
import { createMatchServiceClient } from '~/server/lib/axios'
import { getDb } from '~/server/lib/mongo'

export interface Vacancy {
  id: string
  name: string
  start: Date
}

export async function fetchActiveVacancies(): Promise<Vacancy[]> {
  const db = await getDb()
  const coll = db.collection<Vacancy>('vacancy')
  const documents = coll.find().sort({ name: 1 })
  return documents.toArray()
}

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

export async function createVacancy(vacancy: CreateVacancyInput, event: H3Event) {
  const id = randomUUID()
  const output = { id, ...vacancy }

  const client = await createMatchServiceClient(event)
  const buffer = OpenVacancy.toBuffer(output)
  await client.post('/api/v1/commands', buffer, {
    headers: {
      'X-gm.type': OpenVacancy.name,
    },
  })

  return output
}
