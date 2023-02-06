import 'reflect-metadata'

export interface Message {
  type: string
  payload: any
}

export type MessageHandler = (message: Message) => Promise<void>

export interface EventStore {
  connect: (handler: MessageHandler) => Promise<void>

  disconnect: () => Promise<void>
}

export const EVENT_STORE_TYPE = Symbol.for('EventStore')
