import { ACTUATOR_TYPE, Actuator } from '../actuator'
import {
  EXPRESS_CONFIG_TYPE,
  ExpressActuator,
  ExpressConfig,
} from '../actuator/express'
import { Config, RETRY_CONFIG_TYPE, RetryConfig } from '../config'
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
import { EVENT_DESERIALIZER_TYPE, EventDeserializer } from './events'
import {
  SkillCreatedOrUpdatedType,
  SkillDeletedType,
  VacanciesResetType,
  VacancyCreatedType,
} from './events/asvc'
import { AsvcEventDeserializer } from './events/asvc'
import {
  ContainerEventHandlerRegistry,
  EVENT_HANDLER_REGISTRY_TYPE,
  EventHandler,
  EventHandlerRegistry,
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

export function createContainer(config: Config) {
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
    .bind<EventHandler>(SkillCreatedOrUpdatedType.name!)
    .to(SkillCreatedOrUpdatedHandler)
  container.bind<EventHandler>(SkillDeletedType.name!).to(SkillDeletedHandler)
  container
    .bind<EventHandler>(VacanciesResetType.name!)
    .to(VacanciesResetHandler)
  container
    .bind<EventHandler>(VacancyCreatedType.name!)
    .to(VacancyCreatedHandler)

  container.bind<Application>(APPLICATION_TYPE).to(Application)

  return container
}
