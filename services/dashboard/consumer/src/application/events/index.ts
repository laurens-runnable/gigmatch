import { type Buffer } from 'buffer'

export interface EventDeserializer {
  handlesType: (type: string) => boolean

  deserializeEvent: (type: string, buffer: Buffer) => any
}

export const EVENT_DESERIALIZER_TYPE = Symbol.for('EventDeserializer')
