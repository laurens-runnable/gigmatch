import { SkillDocument } from '~/../shared/mongodb'
import { getDb } from '~/server/lib/mongo'

export async function fetchSkills(): Promise<SkillDocument[]> {
  const db = await getDb()
  const coll = db.collection<SkillDocument>('skill')
  const documents = coll.find().sort({ name: 1 })
  return documents.toArray()
}
