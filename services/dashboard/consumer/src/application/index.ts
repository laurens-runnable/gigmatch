import { ACTUATOR_TYPE, Actuator } from '../actuator'
import { EVENT_STORE_TYPE, EventStore, Message } from '../event-store'
import { LOGGER_TYPE, Logger } from '../logger'
import { REPOSITORY_TYPE, Repository } from '../repository'
import avroTypeRegistry from './avro'
import { createContainer } from './container'
import { EventHandler } from './handlers'
import { Container } from 'inversify'

export class Application {
  private readonly _container: Container

  constructor() {
    this._container = createContainer()
  }

  private logger(): Logger {
    return this._container.get<Logger>(LOGGER_TYPE)
  }

  private eventStore(): EventStore {
    return this._container.get<EventStore>(EVENT_STORE_TYPE)
  }

  private repository(): Repository {
    return this._container.get<Repository>(REPOSITORY_TYPE)
  }

  private actuator(): Actuator {
    return this._container.get<Actuator>(ACTUATOR_TYPE)
  }

  async startup(): Promise<void> {
    this.logger().info('Starting up application')
    await this.repository().open()
    await this.eventStore().connect(async (message) => {
      await this.handleMessage(message)
    })
    await this.actuator().start()
  }

  async shutdown(): Promise<void> {
    this.logger().info('Shutting down application')
    await this.actuator().stop()
    await this.eventStore().disconnect()
    await this.repository().close()
  }

  private async handleMessage(message: Message) {
    const { type } = message

    const avroType = avroTypeRegistry[type]
    if (!avroType || !this._container.isBound(type)) {
      this.logger().error("Unhandled event '%s'", type)
      return
    }

    const payload = avroType.fromBuffer(message.payload)

    this.logger().debug("Handling event '%s'", type)
    const eventHandler = this._container.get<EventHandler>(type)
    await eventHandler.handleEvent({ type, payload })
  }
}
