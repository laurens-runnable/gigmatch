import { REPOSITORY_TYPE, Repository } from '../../repository'
import { Event, EventHandler } from './index'
import { inject, injectable } from 'inversify'

@injectable()
export class VacanciesResetHandler implements EventHandler {
  private readonly _repository: Repository

  constructor(@inject(REPOSITORY_TYPE) repository: Repository) {
    this._repository = repository
  }

  async handleEvent(event: Event): Promise<void> {
    await this._repository.removeAllVacancies()
  }
}

@injectable()
export class VacancyCreatedHandler implements EventHandler {
  private readonly _repository: Repository

  constructor(@inject(REPOSITORY_TYPE) repository: Repository) {
    this._repository = repository
  }

  async handleEvent({ payload }: Event): Promise<void> {
    payload.start = new Date(payload.start)
    await this._repository.updateVacancy(payload)
  }
}
