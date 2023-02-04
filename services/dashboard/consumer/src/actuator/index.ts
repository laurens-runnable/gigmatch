import 'reflect-metadata'

export interface Actuator {
  start(): Promise<void>

  stop(): Promise<void>
}

export const ACTUATOR_TYPE = Symbol.for('Actuator')
