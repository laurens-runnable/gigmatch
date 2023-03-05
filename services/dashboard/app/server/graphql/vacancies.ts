import { randomUUID } from 'crypto'
import { H3Event } from 'h3'
import { CreateVacancy } from '~/server/avro/commands'
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

export type CreateVacancyParams = {
  jobTitle: string
  skillId: string
  start: Date
  end: Date
  rateAmount: number
  rateType: string
  deadline: Date
  listed: boolean
}

export async function createVacancy(
  params: CreateVacancyParams,
  event: H3Event
) {
  const id = randomUUID()
  const {
    jobTitle,
    skillId,
    start,
    end,
    rateAmount,
    rateType,
    deadline,
    listed,
  } = params
  const value = {
    id,
    jobTitle,
    skillId,
    start,
    end,
    rateAmount,
    rateType,
    deadline,
    listed,
  }
  const body = CreateVacancy.toBuffer(value)

  const client = await createMatchServiceClient(event)
  await client.post('/api/v1/commands', body, {
    headers: {
      'X-gm.type': CreateVacancy.name,
    },
  })

  return value
}
