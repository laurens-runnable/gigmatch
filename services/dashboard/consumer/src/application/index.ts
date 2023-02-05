import { ACTUATOR_TYPE, Actuator } from '../actuator'
import { Config } from '../config'
import { EVENT_STORE_TYPE, EventStore, Message } from '../event-store'
import { LOGGER_TYPE, Logger } from '../logger'
import { REPOSITORY_TYPE, Repository } from '../repository'
import { createContainer } from './container'
import { EVENT_DESERIALIZER_TYPE, EventDeserializer } from './events'
import { EVENT_HANDLER_REGISTRY_TYPE, EventHandlerRegistry } from './handlers'
import { inject, injectable } from 'inversify'

export const APPLICATION_TYPE = Symbol.for('Application')

export function createApplication(config: Config) {
  const container = createContainer(config)
  return container.get<Application>(APPLICATION_TYPE)
}

@injectable()
export class Application {
  private readonly _logger: Logger
  private readonly _eventStore: EventStore
  private readonly _repository: Repository
  private readonly _actuator: Actuator
  private readonly _eventHandlerRegistry: EventHandlerRegistry
  private readonly _eventDeserializer: EventDeserializer

  constructor(
    @inject(LOGGER_TYPE) logger: Logger,
    @inject(EVENT_STORE_TYPE) eventStore: EventStore,
    @inject(REPOSITORY_TYPE) repository: Repository,
    @inject(ACTUATOR_TYPE) actuator: Actuator,
    @inject(EVENT_HANDLER_REGISTRY_TYPE)
    eventHandlerRegistry: EventHandlerRegistry,
    @inject(EVENT_DESERIALIZER_TYPE)
    eventDeserializer: EventDeserializer
  ) {
    this._logger = logger
    this._eventStore = eventStore
    this._repository = repository
    this._actuator = actuator
    this._eventHandlerRegistry = eventHandlerRegistry
    this._eventDeserializer = eventDeserializer
  }

  async startup(): Promise<void> {
    this._logger.info('Starting up application')
    await this._repository.open()
    await this._eventStore.connect(async (message) => {
      await this.handleMessage(message)
    })
    await this._actuator.start()
  }

  async shutdown(): Promise<void> {
    this._logger.info('Shutting down application')
    await this._actuator.stop()
    await this._eventStore.disconnect()
    await this._repository.close()
  }

  private async handleMessage(message: Message) {
    const { type } = message

    if (
      !this._eventDeserializer.handlesType(type) ||
      !this._eventHandlerRegistry.containsEventHandler(type)
    ) {
      this._logger.error("Unhandled event '%s'", type)
      return
    }

    const payload = this._eventDeserializer.deserializeEvent(
      type,
      message.payload
    )

    this._logger.debug("Handling event '%s'", type)
    const eventHandler = this._eventHandlerRegistry.resolveEventHandler(type)
    await eventHandler.handleEvent({ type, payload })
  }
}
