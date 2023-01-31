import { Logger } from 'winston'

let loggerInstance: Logger

export function setLogger(logger: Logger) {
  loggerInstance = logger
}

export function getLogger() {
  if (!loggerInstance) {
    throw new Error('Logger is not configured')
  }
  return loggerInstance
}
