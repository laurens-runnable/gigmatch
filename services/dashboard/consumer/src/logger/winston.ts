import { Logger } from './index'
import { injectable } from 'inversify'
import winston from 'winston'

@injectable()
export class WinstonLogger implements Logger {
  private readonly _logger: winston.Logger

  constructor() {
    this._logger = winston.createLogger({
      transports: [new winston.transports.Console()],
      format: winston.format.combine(
        winston.format.splat(),
        winston.format.simple()
      ),
    })
  }

  error(message: string, ...variables: any[]): void {
    this._logger.error(message, ...variables)
  }

  info(message: string, ...variables: any[]): void {
    this._logger.info(message, ...variables)
  }

  debug(message: string, ...variables: any[]): void {
    this._logger.debug(message, ...variables)
  }
}
