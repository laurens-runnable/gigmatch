import { DATA_GENERATOR_TYPE, DataGenerator } from '../data-generator'
import { LOGGER_TYPE, Logger } from '../logger'
import { createContainer } from './container'
import { inject, injectable } from 'inversify'

export interface Config {
  baseUrl: string
  admin: {
    username: string
    password: string
  }
  recruiter: {
    username: string
    password: string
  }
}

@injectable()
export class Application {
  private readonly _logger: Logger
  private readonly _generator: DataGenerator

  constructor(
    @inject(LOGGER_TYPE) logger: Logger,
    @inject(DATA_GENERATOR_TYPE) generator: DataGenerator
  ) {
    this._logger = logger
    this._generator = generator
  }

  async init(config: Config): Promise<void> {
    await this._generator.init(config.baseUrl)
  }

  async generateVacancies(amount: number): Promise<void> {
    this._logger.info('Generating %d vacancies', amount)
    await this._generator.generateVacancies(amount)
  }
}

export const APPLICATION_TYPE = Symbol.for('Application')

export function createApplication(): Application {
  const container = createContainer()
  return container.get<Application>(APPLICATION_TYPE)
}
