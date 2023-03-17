import { type APIRequestContext, expect } from '@playwright/test'
import { type Db, MongoClient } from 'mongodb'
import { v4 as uuidv4 } from 'uuid'

interface MongoConfig {
  readonly url: string
  readonly username: string
  readonly password: string
  readonly database: string
}

let _db: any

async function getMongoDb({
  url,
  username,
  password,
  database,
}: MongoConfig): Promise<Db> {
  if (_db === undefined) {
    url = url.replace(
      /^mongodb:\/\/(.+)$/,
      `mongodb://${username}:${password}@$1`
    )
    const client = await MongoClient.connect(url)
    _db = client.db(database)
  }
  return _db as Db
}

interface TestSetupDocument {
  readonly id: string
  readonly isActive: boolean
}

async function testSetupSynchronization(
  id: string,
  timeout = 5000,
  pollInterval = 100
): Promise<void> {
  const db = await getMongoDb({
    url: 'mongodb://localhost',
    username: 'root',
    password: 'root',
    database: 'gigmatch',
  })

  const collection = db.collection<TestSetupDocument>('test_setup')
  let timePassed = 0
  while (true) {
    const testSetup = await collection.findOne({ id })
    if (testSetup !== null && !testSetup.isActive) {
      await collection.deleteOne({ id })
      break
    }

    if (timePassed >= timeout) {
      throw new Error('Test synchronization timeout')
    }

    await new Promise<void>((resolve) => {
      setTimeout(() => {
        timePassed += pollInterval
        resolve()
      }, pollInterval)
    })
  }
}

export default class DashboardTestSetup {
  private readonly request: APIRequestContext
  private _id: string | null = uuidv4()

  constructor(request: APIRequestContext) {
    this.request = request
  }

  async start(): Promise<void> {
    const post = await this.request.post('/matches/actuator/test-setup-start', {
      data: { id: this._id },
    })
    expect(post.status()).toBe(204)
  }

  async completion(): Promise<void> {
    const post = await this.request.post(
      '/matches/actuator/test-setup-complete',
      { data: { id: this._id } }
    )
    expect(post.status()).toBe(204)
    await testSetupSynchronization(this._id as string)
    this._id = null
  }
}
