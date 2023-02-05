import { getDb } from '~/server/lib/mongo'

export type Skill = {
  id: string
  name: string
  slug: string
}

export async function fetchSkills(): Promise<Skill[]> {
  const db = await getDb()
  const coll = db.collection<Skill>('skill')
  const documents = coll.find().sort({ name: 1 })
  return documents.toArray()
}
