import { DATA_GENERATOR_TYPE, type DataGenerator } from '../data-generator'
import { RandomDataGenerator } from '../data-generator/random'
import { LOGGER_TYPE, type Logger } from '../logger'
import { WinstonLogger } from '../logger/winston'
import { APPLICATION_TYPE, Application } from './index'
import { Container } from 'inversify'

export function createContainer(): Container {
  const container = new Container()

  container.bind<Logger>(LOGGER_TYPE).to(WinstonLogger).inSingletonScope()
  container
    .bind<DataGenerator>(DATA_GENERATOR_TYPE)
    .to(RandomDataGenerator)
    .inSingletonScope()
  container.bind<Application>(APPLICATION_TYPE).to(Application)

  return container
}
