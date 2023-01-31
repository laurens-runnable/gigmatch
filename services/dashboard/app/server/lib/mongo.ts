import { Db, MongoClient } from 'mongodb'

let db: Db

export async function getDb(): Promise<Db> {
  if (!db) {
    const {
      mongo: { url, database },
    } = useRuntimeConfig()
    const client = await MongoClient.connect(url)
    db = client.db(database)
  }
  return db
}
