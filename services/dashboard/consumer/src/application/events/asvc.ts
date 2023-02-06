import SkillCreatedOrUpdatedSchema from './SkillCreatedOrUpdated.avsc.json'
import SkillDeletedSchema from './SkillDeleted.avsc.json'
import VacanciesResetSchema from './VacanciesReset.avsc.json'
import VacancyCreatedSchema from './VacancyCreated.avsc.json'
import { type EventDeserializer } from './index'
import { type Schema, Type } from 'avsc'
import { injectable } from 'inversify'

export const VacanciesResetType = Type.forSchema(VacanciesResetSchema as Schema)
export const VacancyCreatedType = Type.forSchema(VacancyCreatedSchema as Schema)
export const SkillCreatedOrUpdatedType = Type.forSchema(
  SkillCreatedOrUpdatedSchema as Schema
)
export const SkillDeletedType = Type.forSchema(SkillDeletedSchema as Schema)

@injectable()
export class AsvcEventDeserializer implements EventDeserializer {
  private readonly _typesByName: Record<string, Type>

  constructor() {
    this._typesByName = {
      [VacanciesResetType.name as string]: VacanciesResetType,
      [VacancyCreatedType.name as string]: VacancyCreatedType,
      [SkillCreatedOrUpdatedType.name as string]: SkillCreatedOrUpdatedType,
      [SkillDeletedType.name as string]: SkillDeletedType,
    }
  }

  handlesType(type: string): boolean {
    return this._typesByName[type] !== undefined
  }

  deserializeEvent(type: string, buffer: Buffer): any {
    const t = this._typesByName[type]
    return t.fromBuffer(buffer)
  }
}
