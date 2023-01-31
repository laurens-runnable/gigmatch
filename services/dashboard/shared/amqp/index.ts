import { getLogger } from '../log'
import { RetryConfig, createRetryConfig } from '../retry'
import amqplib, { Channel, Connection } from 'amqplib'
import process from 'process'
import promiseRetry from 'promise-retry'

export type AmqpConfig = {
  readonly url: string
  readonly matchEvent: {
    readonly exchange: string
    readonly queue: string
  }
}

class MessageQueue {
  private readonly _connection: Connection
  public readonly channel: Channel
  public readonly queue: string

  constructor(connection: Connection, channel: Channel, queue: string) {
    this._connection = connection
    this.channel = channel
    this.queue = queue
  }

  async close() {
    try {
      await this.channel.close()
    } finally {
      await this._connection.close()
    }
  }
}

export function createAmqpConfig(): AmqpConfig {
  const prefix = 'GIGMAP_AMQP'
  const url = process.env[`${prefix}_URL`] ?? 'amqp://localhost'
  const exchange =
    process.env[`${prefix}_MATCH_EVENT_EXCHANGE`] ?? 'match-events'
  const queue =
    process.env[`${prefix}_MATCH_EVENT_EXCHANGE`] ?? 'match-events.dashboard'

  return {
    url,
    matchEvent: {
      exchange,
      queue,
    },
  }
}

export async function openMessageQueue(
  amqpConfig?: AmqpConfig,
  retryConfig?: RetryConfig
) {
  if (!amqpConfig) {
    amqpConfig = createAmqpConfig()
  }
  if (!retryConfig) {
    retryConfig = createRetryConfig()
  }

  const {
    url,
    matchEvent: { exchange, queue },
  } = amqpConfig

  getLogger().info(`Connecting to ${url}`)

  const { retries, timeout, factor } = retryConfig
  const connection = await promiseRetry(
    (retry) => amqplib.connect(url).catch(retry),
    {
      retries: retries,
      minTimeout: timeout,
      factor: factor,
    }
  )

  const channel = await connection.createChannel()
  await channel.assertExchange(exchange, 'topic', { durable: true })
  await channel.assertQueue(queue, { durable: true })

  getLogger().info(`Using AMQP queue '${queue}'`)
  await channel.bindQueue(queue, exchange, '#')

  return new MessageQueue(connection, channel, queue)
}
