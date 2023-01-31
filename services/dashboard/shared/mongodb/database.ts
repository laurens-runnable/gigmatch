import { getLogger } from '../log'
import { RetryConfig, createRetryConfig } from '../retry'
import { Collection, Db, Document, MongoClient } from 'mongodb'
import promiseRetry from 'promise-retry'

/**
 * Wrapper for accessing Database collections
 */
export class Collections {
  private readonly _db: Db

  constructor(db: Db) {
    this._db = db
  }

  async initialize() {
    getLogger().debug('Creating indexes for collections')
    await this.skill().createIndex('id')
    await this.vacancy().createIndex('id')
  }

  skill<T extends Document = any>(): Collection<T> {
    return this._db.collection<T>('skill')
  }

  vacancy<T extends Document = any>(): Collection<T> {
    return this._db.collection<T>('vacancy')
  }
}

export class Database {
  private readonly _client: MongoClient
  private readonly _db: Db
  readonly collections: Collections

  constructor(client: MongoClient, db: Db) {
    this._client = client
    this._db = db
    this.collections = new Collections(db)
  }

  async close() {
    getLogger().info('Closing Mongo database')
    await this._client.close()
  }
}

export type MongoConfig = {
  readonly url: string
  readonly database: string
  readonly username?: string
  readonly password?: string
  readonly retry: RetryConfig
}

/**
 * Creates a Config from environment variables.
 */
export function createMongoConfig(): MongoConfig {
  const prefix = 'GIGMATCH_MONGO'
  const url = process.env[`${prefix}_URL`] ?? 'mongodb://localhost'
  const database = process.env[`${prefix}_DATABASE`] ?? 'gigmatch'
  const username = process.env[`${prefix}_USERNAME`] ?? 'root'
  const password = process.env[`${prefix}_PASSWORD`] ?? 'root'
  const retry = createRetryConfig()

  return {
    url,
    database,
    username,
    password,
    retry,
  }
}

/**
 * Opens a Database using the given configuration.
 */
export async function openMongoDatabase(
  mongoConfig?: MongoConfig,
  retryConfig?: RetryConfig
) {
  if (!mongoConfig) {
    mongoConfig = createMongoConfig()
  }
  getLogger().info(`Connecting to ${mongoConfig.url} `)

  let { url } = mongoConfig
  const { username, password } = mongoConfig
  if (username && password) {
    url = url.replace(
      /^mongodb:\/\/(.+)$/,
      `mongodb://${username}:${password}@$1`
    )
  }

  if (!retryConfig) {
    retryConfig = createRetryConfig()
  }

  const { retries, timeout, factor } = retryConfig

  const client = await promiseRetry(
    (retry: any) => MongoClient.connect(url).catch(retry),
    {
      retries,
      minTimeout: timeout,
      factor,
    }
  )

  const { database } = mongoConfig
  getLogger().info(`Using database ${database}`)
  return new Database(client, client.db(database))
}
