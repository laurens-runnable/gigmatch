import 'reflect-metadata'

export interface Logger {
  error(message: string, ...variables: any[]): void

  info(message: string, ...variables: any[]): void

  debug(message: string, ...variables: any[]): void
}

export const LOGGER_TYPE = Symbol.for('Logger')
