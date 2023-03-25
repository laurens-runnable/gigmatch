export interface Skill {
  id: string
  name: string
  slug: string
}

export async function querySkills(): Promise<Skill[]> {
  const db = await useDb()
  const coll = db.collection<Skill>('skill')
  const documents = coll.find().sort({ name: 1 })
  return documents.toArray()
}
