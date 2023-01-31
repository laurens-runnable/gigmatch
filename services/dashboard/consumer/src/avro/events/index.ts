import { Type, Schema } from 'avsc'
import VacancyCreatedSchema from './VacancyCreated.avsc.json'
import VacanciesResetSchema from './VacanciesReset.avsc.json'
import SkillCreatedOrUpdatedSchema from './SkillCreatedOrUpdated.avsc.json'
import SkillDeletedSchema from './SkillDeleted.avsc.json'

export const VacanciesReset = Type.forSchema(VacanciesResetSchema as Schema)
export const VacancyCreated = Type.forSchema(VacancyCreatedSchema as Schema)
export const SkillCreatedOrUpdated = Type.forSchema(SkillCreatedOrUpdatedSchema as Schema)
export const SkillDeleted = Type.forSchema(SkillDeletedSchema as Schema)

type EventTypeRegistry = { [key: string]: Type }

export default {
  [VacanciesReset.name!]: VacanciesReset,
  [VacancyCreated.name!]: VacancyCreated,
  [SkillCreatedOrUpdated.name!]: SkillCreatedOrUpdated,
  [SkillDeleted.name!]: SkillDeleted,
} as EventTypeRegistry
