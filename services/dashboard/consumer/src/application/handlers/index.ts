import { Container, injectable } from 'inversify'

export interface Event {
  type: string
  payload: any
}

export interface EventHandler {
  handleEvent: (event: Event) => Promise<void>
}

export const EVENT_HANDLER_REGISTRY_TYPE = Symbol.for('EventHandlerRegistry')

export interface EventHandlerRegistry {
  containsEventHandler: (type: string) => boolean

  resolveEventHandler: (type: string) => EventHandler
}

@injectable()
export class ContainerEventHandlerRegistry implements EventHandlerRegistry {
  private readonly _container: Container

  constructor(container: Container) {
    this._container = container
  }

  containsEventHandler(type: string): boolean {
    return this._container.isBound(type)
  }

  resolveEventHandler(type: string): EventHandler {
    return this._container.get<EventHandler>(type)
  }
}
