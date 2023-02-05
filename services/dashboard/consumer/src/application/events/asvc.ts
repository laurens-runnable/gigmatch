import SkillCreatedOrUpdatedSchema from './SkillCreatedOrUpdated.avsc.json'
import SkillDeletedSchema from './SkillDeleted.avsc.json'
import VacanciesResetSchema from './VacanciesReset.avsc.json'
import VacancyCreatedSchema from './VacancyCreated.avsc.json'
import { EventDeserializer } from './index'
import { Schema, Type } from 'avsc'
import { injectable } from 'inversify'

export const VacanciesResetType = Type.forSchema(VacanciesResetSchema as Schema)
export const VacancyCreatedType = Type.forSchema(VacancyCreatedSchema as Schema)
export const SkillCreatedOrUpdatedType = Type.forSchema(
  SkillCreatedOrUpdatedSchema as Schema
)
export const SkillDeletedType = Type.forSchema(SkillDeletedSchema as Schema)

@injectable()
export class AsvcEventDeserializer implements EventDeserializer {
  private readonly _typesByName: { [key: string]: Type }

  constructor() {
    this._typesByName = {
      [VacanciesResetType.name!]: VacanciesResetType,
      [VacancyCreatedType.name!]: VacancyCreatedType,
      [SkillCreatedOrUpdatedType.name!]: SkillCreatedOrUpdatedType,
      [SkillDeletedType.name!]: SkillDeletedType,
    }
  }

  handlesType(type: string): boolean {
    return this._typesByName.hasOwnProperty(type)
  }

  deserializeEvent(type: string, buffer: Buffer): any {
    const t = this._typesByName[type]
    return t.fromBuffer(buffer)
  }
}
