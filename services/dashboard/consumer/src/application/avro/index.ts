import SkillCreatedOrUpdatedSchema from './SkillCreatedOrUpdated.avsc.json'
import SkillDeletedSchema from './SkillDeleted.avsc.json'
import VacanciesResetSchema from './VacanciesReset.avsc.json'
import VacancyCreatedSchema from './VacancyCreated.avsc.json'
import { Schema, Type } from 'avsc'

export const VacanciesResetType = Type.forSchema(VacanciesResetSchema as Schema)
export const VacancyCreatedType = Type.forSchema(VacancyCreatedSchema as Schema)
export const SkillCreatedOrUpdatedType = Type.forSchema(
  SkillCreatedOrUpdatedSchema as Schema
)
export const SkillDeletedType = Type.forSchema(SkillDeletedSchema as Schema)

type AvroTypeRegistry = { [key: string]: Type }

export default {
  [VacanciesResetType.name!]: VacanciesResetType,
  [VacancyCreatedType.name!]: VacancyCreatedType,
  [SkillCreatedOrUpdatedType.name!]: SkillCreatedOrUpdatedType,
  [SkillDeletedType.name!]: SkillDeletedType,
} as AvroTypeRegistry
