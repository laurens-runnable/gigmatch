import { LOGGER_TYPE } from '../logger'
import { WinstonLogger } from '../logger/winston'
import { type EventStore, type MessageHandler } from './index'
import { inject, injectable } from 'inversify'
import { type Consumer, Kafka, logLevel } from 'kafkajs'

export interface KafkaConfig {
  readonly clientId: string
  readonly brokers: string[]
  readonly groupId: string
  readonly topic: string
}

export const KAFKA_CONFIG_TYPE = Symbol.for('KafkaConfig')

const toWinstonLogLevel = (level: logLevel): string => {
  switch (level) {
    case logLevel.ERROR:
    case logLevel.NOTHING:
      return 'error'
    case logLevel.WARN:
      return 'warn'
    case logLevel.INFO:
      return 'info'
    case logLevel.DEBUG:
      return 'debug'
  }
}

@injectable()
export class KafkaEventStore implements EventStore {
  private readonly _config: KafkaConfig
  private readonly _logger: WinstonLogger
  private _consumer: Consumer | null = null

  constructor(
    @inject(KAFKA_CONFIG_TYPE) config: KafkaConfig,
    @inject(LOGGER_TYPE) logger: WinstonLogger
  ) {
    this._config = config
    this._logger = logger
  }

  private _createLog(): any {
    const winstonLogger = this._logger.getWinstonLogger()
    return ({ level, log }: any) => {
      const { message, ...extra } = log
      winstonLogger.log({
        level: toWinstonLogLevel(level),
        message,
        extra,
      })
    }
  }

  async connect(eventHandler: MessageHandler): Promise<void> {
    const { clientId, brokers } = this._config

    const kafka = new Kafka({
      clientId,
      brokers,
      logCreator: () => this._createLog(),
    })

    const { groupId } = this._config
    this._consumer = kafka.consumer({ groupId })

    this._logger.info(
      "Connecting to brokers %s and consumer group '%s'",
      brokers,
      groupId
    )
    await this._consumer.connect()

    const { topic } = this._config
    this._logger.info("Subscribing to topic '%s'", topic)
    await this._consumer.subscribe({ topic })

    await this._consumer.run({
      eachMessage: async ({ message }) => {
        if (message.headers != null) {
          const type = message.headers['gm.type']
          const payload = message.value
          if (type !== undefined && payload !== null) {
            await eventHandler({ type: type.toString(), payload })
          }
        }
      },
    })
  }

  async disconnect(): Promise<void> {
    if (this._consumer != null) {
      await this._consumer.stop()
      await this._consumer.disconnect()
      this._consumer = null
    }
  }
}
