import { LOGGER_TYPE, Logger } from '../logger'
import { Actuator } from './'
import express, { Express } from 'express'
import actuator from 'express-actuator'
import { Server } from 'http'
import { inject, injectable } from 'inversify'

export type ExpressConfig = {
  port: number
}

export const EXPRESS_CONFIG_TYPE = Symbol.for('ExpressConfig')

@injectable()
export class ExpressActuator implements Actuator {
  private readonly _app: Express
  private readonly _logger: Logger
  private readonly _config: ExpressConfig
  private _server: Server | null = null

  constructor(
    @inject(LOGGER_TYPE) logger: Logger,
    @inject(EXPRESS_CONFIG_TYPE) config: ExpressConfig
  ) {
    this._app = express()
    this._app.use(actuator())
    this._logger = logger
    this._config = config
  }

  start(): Promise<void> {
    return new Promise((resolve) => {
      this._logger.info('Starting Actuator on port %d', this._config.port)
      this._server = this._app.listen(this._config.port, () => {
        resolve()
      })
    })
  }

  stop(): Promise<void> {
    if (this._server) {
      return new Promise((resolve) => {
        this._logger.info('Stopping Actuator')
        this._server?.close(() => {
          this._server = null
          resolve()
        })
      })
    } else {
      return Promise.resolve()
    }
  }
}
