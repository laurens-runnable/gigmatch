import { REPOSITORY_TYPE, Repository } from '../../repository'
import { type Event, type EventHandler } from './index'
import { inject, injectable } from 'inversify'

@injectable()
export class TestSetupStartedHandler implements EventHandler {
  private readonly _repository: Repository

  constructor(@inject(REPOSITORY_TYPE) repository: Repository) {
    this._repository = repository
  }

  async handleEvent({ payload }: Event): Promise<void> {
    await this._repository.startTestSetup(payload.id)
  }
}

@injectable()
export class TestSetupCompletedHandler implements EventHandler {
  private readonly _repository: Repository

  constructor(@inject(REPOSITORY_TYPE) repository: Repository) {
    this._repository = repository
  }

  async handleEvent({ payload }: Event): Promise<void> {
    await this._repository.completeTestSetup(payload.id)
  }
}
