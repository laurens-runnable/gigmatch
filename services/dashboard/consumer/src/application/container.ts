import { ACTUATOR_TYPE, Actuator } from '../actuator'
import {
  EXPRESS_CONFIG_TYPE,
  ExpressActuator,
  ExpressConfig,
} from '../actuator/express'
import { EVENT_STORE_TYPE, EventStore } from '../event-store'
import {
  KAFKA_CONFIG_TYPE,
  KafkaConfig,
  KafkaEventStore,
} from '../event-store/kafka'
import { LOGGER_TYPE, Logger } from '../logger'
import { WinstonLogger } from '../logger/winston'
import { REPOSITORY_TYPE, Repository } from '../repository'
import {
  MONGO_CONFIG_TYPE,
  MongoConfig,
  MongoRepository,
} from '../repository/mongo'
import { SkillCreatedOrUpdatedType, SkillDeletedType, VacanciesResetType, VacancyCreatedType } from "./avro";
import { RETRY_CONFIG_TYPE, RetryConfig, createConfig } from './config'
import { EventHandler } from './handlers'
import {
  SkillCreatedOrUpdatedHandler,
  SkillDeletedHandler,
} from './handlers/skills'
import { Container } from 'inversify'
import { VacanciesResetHandler, VacancyCreatedHandler } from "./handlers/vacancies";

export function createContainer() {
  const container = new Container()
  const config = createConfig()

  container.bind<RetryConfig>(RETRY_CONFIG_TYPE).toConstantValue(config.retry)

  container.bind<Logger>(LOGGER_TYPE).to(WinstonLogger).inSingletonScope()

  container
    .bind<ExpressConfig>(EXPRESS_CONFIG_TYPE)
    .toConstantValue(config.express)
  container.bind<Actuator>(ACTUATOR_TYPE).to(ExpressActuator).inSingletonScope()

  container.bind<MongoConfig>(MONGO_CONFIG_TYPE).toConstantValue(config.mongo)
  container
    .bind<Repository>(REPOSITORY_TYPE)
    .to(MongoRepository)
    .inSingletonScope()

  container.bind<KafkaConfig>(KAFKA_CONFIG_TYPE).toConstantValue(config.kafka)
  container
    .bind<EventStore>(EVENT_STORE_TYPE)
    .to(KafkaEventStore)
    .inSingletonScope()

  container
    .bind<EventHandler>(SkillCreatedOrUpdatedType.name!)
    .to(SkillCreatedOrUpdatedHandler)
    .inSingletonScope()
  container
    .bind<EventHandler>(SkillDeletedType.name!)
    .to(SkillDeletedHandler)
    .inSingletonScope()

  container
    .bind<EventHandler>(VacanciesResetType.name!)
    .to(VacanciesResetHandler)
    .inSingletonScope()
  container
    .bind<EventHandler>(VacancyCreatedType.name!)
    .to(VacancyCreatedHandler)
    .inSingletonScope()

  return container
}
