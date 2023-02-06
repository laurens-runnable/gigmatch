import { RETRY_CONFIG_TYPE, RetryConfig } from '../config'
import { LOGGER_TYPE, Logger } from '../logger'
import { type Repository, type Skill, type Vacancy } from './index'
import { inject, injectable } from 'inversify'
import { type Collection, type Db, MongoClient } from 'mongodb'
import promiseRetry from 'promise-retry'

export interface MongoConfig {
  readonly url: string
  readonly username: string
  readonly password: string
  readonly database: string
  readonly useTestSetupCollection: boolean
}

export const MONGO_CONFIG_TYPE = Symbol.for('MongoConfigg')

interface TestSetup {
  id: string
  isActive: boolean
}

@injectable()
export class MongoRepository implements Repository {
  private readonly _mongoConfig: MongoConfig
  private readonly _retryConfig: RetryConfig
  private readonly _logger: Logger
  private _client: MongoClient | null = null
  private _db: Db | null = null
  private _skillCollection?: Collection<Skill>
  private _vacancyCollection?: Collection<Vacancy>
  private _testSetupCollection?: Collection<TestSetup>

  constructor(
    @inject(MONGO_CONFIG_TYPE) mongoConfig: MongoConfig,
    @inject(RETRY_CONFIG_TYPE) retryConfig: RetryConfig,
    @inject(LOGGER_TYPE) logger: Logger
  ) {
    this._mongoConfig = mongoConfig
    this._retryConfig = retryConfig
    this._logger = logger
  }

  private resolveConnectionUrl(): string {
    let { url } = this._mongoConfig
    this._logger.info('Connecting to %s', url)

    const { username, password } = this._mongoConfig
    url = url.replace(
      /^mongodb:\/\/(.+)$/,
      `mongodb://${username}:${password}@$1`
    )

    return url
  }

  async open(): Promise<void> {
    const url = this.resolveConnectionUrl()
    const { retries, timeout, factor } = this._retryConfig

    const client = await promiseRetry(
      async (retry: any) => await MongoClient.connect(url).catch(retry),
      {
        retries,
        minTimeout: timeout,
        factor,
      }
    )
    const { database } = this._mongoConfig
    this._logger.info("Using database '%s'", database)
    this._client = client
    this._db = client.db(database)
    this._skillCollection = this._db.collection<Skill>('skill')
    this._vacancyCollection = this._db.collection<Vacancy>('vacancy')
    if (this._mongoConfig.useTestSetupCollection) {
      this._testSetupCollection = this._db?.collection<TestSetup>('test_setup')
    }
    await this._skillCollection.createIndex('id')
    await this._vacancyCollection.createIndex('id')
  }

  async close(): Promise<void> {
    await this._client?.close()
    this._db = null
    this._client = null
  }

  async removeSkill(id: string): Promise<void> {
    await this._skillCollection?.deleteOne({ id })
  }

  async updateSkill(skill: Skill): Promise<void> {
    await this._skillCollection?.findOneAndUpdate(
      { id: skill.id },
      { $set: skill },
      { upsert: true }
    )
  }

  async removeAllVacancies(): Promise<void> {
    await this._vacancyCollection?.deleteMany({})
  }

  async updateVacancy(vacancy: Vacancy): Promise<void> {
    await this._vacancyCollection?.insertOne(vacancy)
  }

  async startTestSetup(id: string): Promise<void> {
    await this._testSetupCollection?.insertOne({
      id,
      isActive: true,
    })
  }

  async completeTestSetup(id: string): Promise<void> {
    await this._testSetupCollection?.findOneAndUpdate(
      { id },
      {
        $set: {
          isActive: false
        }
      })
  }
}
