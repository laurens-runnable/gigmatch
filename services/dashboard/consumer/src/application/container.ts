import { ACTUATOR_TYPE, type Actuator } from '../actuator'
import {
  EXPRESS_CONFIG_TYPE,
  ExpressActuator,
  type ExpressConfig,
} from '../actuator/express'
import { type Config, RETRY_CONFIG_TYPE, type RetryConfig } from '../config'
import { EVENT_STORE_TYPE, type EventStore } from '../event-store'
import {
  KAFKA_CONFIG_TYPE,
  type KafkaConfig,
  KafkaEventStore,
} from '../event-store/kafka'
import { LOGGER_TYPE, type Logger } from '../logger'
import { WinstonLogger } from '../logger/winston'
import { REPOSITORY_TYPE, type Repository } from '../repository'
import {
  MONGO_CONFIG_TYPE,
  type MongoConfig,
  MongoRepository,
} from '../repository/mongo'
import { EVENT_DESERIALIZER_TYPE, type EventDeserializer } from './events'
import {
  AsvcEventDeserializer,
  SkillCreatedOrUpdatedType,
  SkillDeletedType,
  VacanciesResetType,
  VacancyCreatedType,
} from './events/asvc'
import {
  ContainerEventHandlerRegistry,
  EVENT_HANDLER_REGISTRY_TYPE,
  type EventHandler,
  type EventHandlerRegistry,
} from './handlers'
import {
  SkillCreatedOrUpdatedHandler,
  SkillDeletedHandler,
} from './handlers/skills'
import {
  VacanciesResetHandler,
  VacancyCreatedHandler,
} from './handlers/vacancies'
import { APPLICATION_TYPE, Application } from './index'
import { Container } from 'inversify'

export function createContainer(config: Config): Container {
  const container = new Container()

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
  container.bind<EventStore>(EVENT_STORE_TYPE).to(KafkaEventStore)

  container.bind(Container).toDynamicValue((ctx) => ctx.container as Container)
  container
    .bind<EventHandlerRegistry>(EVENT_HANDLER_REGISTRY_TYPE)
    .to(ContainerEventHandlerRegistry)

  container
    .bind<EventDeserializer>(EVENT_DESERIALIZER_TYPE)
    .to(AsvcEventDeserializer)

  container
    .bind<EventHandler>(SkillCreatedOrUpdatedType.name as string)
    .to(SkillCreatedOrUpdatedHandler)
  container
    .bind<EventHandler>(SkillDeletedType.name as string)
    .to(SkillDeletedHandler)
  container
    .bind<EventHandler>(VacanciesResetType.name as string)
    .to(VacanciesResetHandler)
  container
    .bind<EventHandler>(VacancyCreatedType.name as string)
    .to(VacancyCreatedHandler)

  container.bind<Application>(APPLICATION_TYPE).to(Application)

  return container
}
