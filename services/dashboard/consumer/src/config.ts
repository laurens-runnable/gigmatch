import { type ExpressConfig } from './actuator/express'
import { type KafkaConfig } from './event-store/kafka'
import { type MongoConfig } from './repository/mongo'

export interface RetryConfig {
  readonly retries: number
  readonly factor: number
  readonly timeout: number
}

export const RETRY_CONFIG_TYPE = Symbol.for('RetryConfig')

export interface TestSynchonizationConfig {
  readonly isEnabled: boolean
}

export interface Config {
  readonly mongo: MongoConfig
  readonly kafka: KafkaConfig
  readonly express: ExpressConfig
  readonly retry: RetryConfig
  readonly testSynchronization: TestSynchonizationConfig
}

export function createConfig(): Config {
  const port = Number(process.env.GIGMATCH_ACTUATOR_PORT ?? 3001)

  const isTestSynchronizationEnabled = (process.env.GIGMATCH_TEST_SYNCHRONIZATION ?? 'false') === 'true'
  return {
    mongo: {
      url: process.env.GIGMATCH_MONGO_URL ?? 'mongodb://localhost',
      username: process.env.GIGMATCH_MONGO_USERNAME ?? 'root',
      password: process.env.GIGMATCH_MONGO_PASSWORD ?? 'root',
      database: process.env.GIGMATCH_MONGO_DATABASE ?? 'gigmatch',
      useTestSetupCollection: isTestSynchronizationEnabled,
    },
    kafka: {
      clientId: process.env.GIGMATCH_KAFKA_CLIENT_ID ?? 'dashboard-consumer',
      brokers: [process.env.GIGMATCH_KAFKA_BROKER ?? 'localhost:9092'],
      groupId: process.env.GIGMATCH_KAFKA_GROUP_ID ?? 'dashboard',
      topic: process.env.GIGMATCH_KAFKA_TOPIC ?? 'match-events',
    },
    express: {
      port,
    },
    retry: {
      retries: Number(process.env.GIGMATCH_RETRY_RETRIES ?? 10),
      timeout: Number(process.env.GIGMATCH_RETRY_TIMEOUT ?? 1000),
      factor: Number(process.env.GIGMATCH_RETRY_FACTOR ?? 2),
    },
    testSynchronization: {
      isEnabled: isTestSynchronizationEnabled,
    },
  }
}
