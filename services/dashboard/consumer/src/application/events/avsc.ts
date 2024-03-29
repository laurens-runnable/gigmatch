import SkillCreatedOrUpdatedSchema from './SkillCreatedOrUpdated.avsc.json'
import SkillDeletedSchema from './SkillDeleted.avsc.json'
import TestSetupCompletedSchema from './TestSetupCompleted.avsc.json'
import TestSetupStartedSchema from './TestSetupStarted.avsc.json'
import VacanciesResetSchema from './VacanciesReset.avsc.json'
import VacancyOpenedSchema from './VacancyOpened.avsc.json'
import { type EventDeserializer } from './index'
import { type Schema, Type, types } from 'avsc'
import { injectable } from 'inversify'

class LogicalDateType extends types.LogicalType {
  _fromValue(val: any): Date | undefined {
    return val !== undefined ? new Date(val * (3600 * 24 * 1000)) : undefined
  }
}

export const VacanciesResetType = Type.forSchema(VacanciesResetSchema as Schema)
export const VacancyOpenedType = Type.forSchema(VacancyOpenedSchema as Schema, {
  logicalTypes: { date: LogicalDateType },
})
export const SkillCreatedOrUpdatedType = Type.forSchema(
  SkillCreatedOrUpdatedSchema as Schema
)
export const SkillDeletedType = Type.forSchema(SkillDeletedSchema as Schema)
export const TestSetupStartedType = Type.forSchema(
  TestSetupStartedSchema as Schema
)
export const TestSetupCompletedType = Type.forSchema(
  TestSetupCompletedSchema as Schema
)

@injectable()
export class AsvcEventDeserializer implements EventDeserializer {
  private readonly _typesByName: Record<string, Type>

  constructor() {
    this._typesByName = {
      [VacanciesResetType.name as string]: VacanciesResetType,
      [VacancyOpenedType.name as string]: VacancyOpenedType,
      [SkillCreatedOrUpdatedType.name as string]: SkillCreatedOrUpdatedType,
      [SkillDeletedType.name as string]: SkillDeletedType,
      [TestSetupStartedType.name as string]: TestSetupStartedType,
      [TestSetupCompletedType.name as string]: TestSetupCompletedType,
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
