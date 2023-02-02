import { randomUUID } from 'crypto'
import { H3Event } from 'h3'
import { VacancyDocument } from '~/../shared/mongodb'
import { toDateInt } from '~/server/avro'
import { CreateVacancy } from '~/server/avro/commands'
import { createMatchServiceClient } from '~/server/lib/axios'
import { getDb } from '~/server/lib/mongo'

export async function fetchActiveVacancies(): Promise<VacancyDocument[]> {
  const db = await getDb()
  const coll = db.collection<VacancyDocument>('vacancy')
  const documents = coll.find().sort({ name: 1 })
  return documents.toArray()
}

export type CreateVacancyParams = {
  name: string
  start: Date
}

export async function createVacancy(
  params: CreateVacancyParams,
  event: H3Event
) {
  const id = randomUUID()
  const { name, start } = params
  const body = CreateVacancy.toBuffer({
    id,
    name,
    start: toDateInt(start),
  })

  const client = await createMatchServiceClient(event)
  await client.post('/api/v1/commands', body, {
    headers: {
      'X-gm.type': CreateVacancy.name,
    },
  })

  return {
    id,
    name,
    start,
  }
}