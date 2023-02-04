import { ExpressConfig } from '../actuator/express'
import { KafkaConfig } from '../event-store/kafka'
import { MongoConfig } from '../repository/mongo'

const process = require('process')

export type EurekaConfig = {
  readonly enabled: boolean
  readonly instance: {
    readonly app: string
    readonly port: number
  }
  readonly eureka: {
    readonly host: string
    readonly port: number
    readonly servicePath: '/eureka/apps/'
  }
}

export type RetryConfig = {
  retries: number
  factor: number
  timeout: number
}

export const RETRY_CONFIG_TYPE = Symbol.for('RetryConfig')

export type Config = {
  readonly eureka: EurekaConfig
  readonly mongo: MongoConfig
  readonly kafka: KafkaConfig
  readonly express: ExpressConfig
  readonly retry: RetryConfig
}

export function createConfig(): Config {
  const port = process.env['GIGMATCH_ACTUATOR_PORT'] ?? 3001

  return {
    eureka: {
      enabled: (process.env['GIGMATCH_EUREKA_ENABLED'] ?? 'false') == 'true',
      instance: {
        app: 'admin-consumer',
        port,
      },
      eureka: {
        host: process.env['GIGMATCH_EUREKA_HOST'] ?? 'localhost',
        port: process.env['GIGMATCH_EUREKA_PORT'] ?? 8070,
        servicePath:
          process.env['GIGMATCH_EUREKA_SERVICE_PATH'] ?? '/eureka/apps/',
      },
    },
    mongo: {
      url: process.env['GIGMATCH_MONGO_URL'] ?? 'mongodb://localhost:27017',
      username: process.env['GIGMATCH_MONGO_USERNAME'] ?? 'root',
      password: process.env['GIGMATCH_MONGO_PASSWORD'] ?? 'root',
      database: process.env['GIGMATCH_MONGO_DATABASE'] ?? 'gigmatch',
    },
    kafka: {
      clientId: process.env['GIGMATCH_KAFKA_CLIENT_ID'] ?? 'dashboard-consumer',
      brokers: [process.env['GIGMATCH_KAFKA_BROKER'] ?? 'localhost:9092'],
      groupId: process.env['GIGMATCH_KAFKA_GROUP_ID'] ?? 'dashboard',
      topic: process.env['GIGMATCH_KAFKA_TOPIC'] ?? 'match-events',
    },
    express: {
      port,
    },
    retry: {
      retries: Number(process.env.GIGMATCH_RETRY_RETRIES ?? 10),
      timeout: Number(process.env.GIGMATCH_RETRY_TIMEOUT ?? 1000),
      factor: Number(process.env.GIGMATCH_RETRY_FACTOR ?? 2),
    },
  }
}
