import { LOGGER_TYPE, Logger } from '../logger'
import { EventStore, MessageHandler } from './index'
import { inject, injectable } from 'inversify'
import { Consumer, Kafka } from 'kafkajs'

export type KafkaConfig = {
  readonly clientId: string
  readonly brokers: string[]
  readonly groupId: string
  readonly topic: string
}

export const KAFKA_CONFIG_TYPE = Symbol.for('KafkaConfig')

@injectable()
export class KafkaEventStore implements EventStore {
  private readonly _config: KafkaConfig
  private readonly _logger: Logger
  private _consumer: Consumer | null = null

  constructor(
    @inject(KAFKA_CONFIG_TYPE) config: KafkaConfig,
    @inject(LOGGER_TYPE) logger: Logger
  ) {
    this._config = config
    this._logger = logger
  }

  async connect(eventHandler: MessageHandler): Promise<void> {
    const { clientId, brokers } = this._config
    const kafka = new Kafka({ clientId, brokers })

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

    this._consumer
      .run({
        eachMessage: async ({ message }) => {
          if (message.headers) {
            const type = message.headers['gm.type']
            const payload = message.value
            if (type && payload) {
              await eventHandler({ type: type.toString(), payload })
            }
          }
        },
      })
      .then(() => {})
  }

  async disconnect(): Promise<void> {
    if (this._consumer) {
      await this._consumer.stop()
      await this._consumer.disconnect()
      this._consumer = null
    }
  }
}
