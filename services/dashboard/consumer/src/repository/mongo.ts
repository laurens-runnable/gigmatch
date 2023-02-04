import { RETRY_CONFIG_TYPE, RetryConfig } from '../application/config'
import { LOGGER_TYPE, Logger } from '../logger'
import { Repository, Skill, Vacancy } from './index'
import { inject, injectable } from 'inversify'
import { Collection, Db, MongoClient } from 'mongodb'
import promiseRetry from 'promise-retry'

export type MongoConfig = {
  readonly url: string
  readonly username: string
  readonly password: string
  readonly database: string
}

export const MONGO_CONFIG_TYPE = Symbol.for('MongoConfigg')

@injectable()
export class MongoRepository implements Repository {
  private readonly _mongoConfig: MongoConfig
  private readonly _retryConfig: RetryConfig
  private readonly _logger: Logger
  private _client: MongoClient | null = null
  private _db: Db | null = null

  constructor(
    @inject(MONGO_CONFIG_TYPE) mongoConfig: MongoConfig,
    @inject(RETRY_CONFIG_TYPE) retryConfig: RetryConfig,
    @inject(LOGGER_TYPE) logger: Logger
  ) {
    this._mongoConfig = mongoConfig
    this._retryConfig = retryConfig
    this._logger = logger
  }

  private resolveConnectionUrl() {
    let { url } = this._mongoConfig
    this._logger.info('Connecting to %s', url)

    const { username, password } = this._mongoConfig
    if (username && password) {
      url = url.replace(
        /^mongodb:\/\/(.+)$/,
        `mongodb://${username}:${password}@$1`
      )
    }

    return url
  }

  async open(): Promise<void> {
    const url = this.resolveConnectionUrl()
    const { retries, timeout, factor } = this._retryConfig

    let client = await promiseRetry(
      (retry: any) => MongoClient.connect(url).catch(retry),
      {
        retries,
        minTimeout: timeout,
        factor,
      }
    )
    if (client) {
      const { database } = this._mongoConfig
      this._logger.info("Using database '%s'", database)
      this._client = client
      this._db = client.db(database)
    }
  }

  async close(): Promise<void> {
    await this._client?.close()
    this._db = null
    this._client = null
  }

  private skillCollection(): Collection<Skill> {
    return this._db!.collection<Skill>('skill')
  }

  async removeSkill(id: string): Promise<void> {
    await this.skillCollection().deleteOne({ id })
  }

  async updateSkill(skill: Skill): Promise<void> {
    await this.skillCollection().findOneAndUpdate(
      { id: skill.id },
      { $set: skill },
      { upsert: true }
    )
  }

  private vacancyCollection(): Collection<Vacancy> {
    return this._db!.collection<Vacancy>('vacancy');
  }

  async removeAllVacancies(): Promise<void> {
    await this.vacancyCollection().deleteMany({});
  }

  async updateVacancy(vacancy: Vacancy): Promise<void> {
    await this.vacancyCollection().insertOne(vacancy);
  }

}
