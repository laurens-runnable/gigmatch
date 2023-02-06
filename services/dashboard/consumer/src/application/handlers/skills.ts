import { REPOSITORY_TYPE, Repository } from '../../repository'
import { type Event, type EventHandler } from './index'
import { inject, injectable } from 'inversify'

@injectable()
export class SkillCreatedOrUpdatedHandler implements EventHandler {
  private readonly _repository: Repository

  constructor(@inject(REPOSITORY_TYPE) repository: Repository) {
    this._repository = repository
  }

  async handleEvent(event: Event): Promise<void> {
    const { id, name, slug } = event.payload
    await this._repository.updateSkill({ id, name, slug })
  }
}

@injectable()
export class SkillDeletedHandler implements EventHandler {
  private readonly _repository: Repository

  constructor(@inject(REPOSITORY_TYPE) repository: Repository) {
    this._repository = repository
  }

  async handleEvent(event: Event): Promise<void> {
    await this._repository.removeSkill(event.payload.id)
  }
}
