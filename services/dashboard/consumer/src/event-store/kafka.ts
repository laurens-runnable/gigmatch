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

  private async _monitorIfTopicExists(
    kafka: Kafka,
    timeout = 60000,
    pollInterval = 1000
  ): Promise<void> {
    const { topic } = this._config
    const admin = kafka.admin()
    let topicExists = false
    let timePassed = 0

    do {
      const before = new Date().getUTCMilliseconds()
      const topics = await admin.listTopics()
      topicExists = topics.includes(topic)
      const after = new Date().getUTCMilliseconds()
      timePassed += after - before
      if (!topicExists) {
        this._logger.info("Waiting for topic '%s'", topic)
      }
      await new Promise((resolve) => {
        setTimeout(resolve, pollInterval)
      })
    } while (!topicExists && timePassed < timeout)

    if (!topicExists && timePassed > timeout) {
      this._logger.error("Timeout waiting for topic '%s'", topic)
      throw new Error('Timeout')
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

    await this._monitorIfTopicExists(kafka)

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
