import { type VacancyDocument } from '../../../../shared/mongo'
import { REPOSITORY_TYPE, Repository } from '../../repository'
import { type Event, type EventHandler } from './index'
import { inject, injectable } from 'inversify'

@injectable()
export class VacanciesResetHandler implements EventHandler {
  private readonly _repository: Repository

  constructor(@inject(REPOSITORY_TYPE) repository: Repository) {
    this._repository = repository
  }

  async handleEvent(_: Event): Promise<void> {
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
    const vacancy: VacancyDocument = {
      ...payload,
      status: 'OPEN',
    }

    await this._repository.updateVacancy(vacancy)
  }
}
