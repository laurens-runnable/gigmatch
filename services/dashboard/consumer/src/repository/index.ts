import { type SkillDocument, type VacancyDocument } from '../../../shared/mongo'
import 'reflect-metadata'

export interface Repository {
  open: () => Promise<void>

  close: () => Promise<void>

  updateSkill: (skill: SkillDocument) => Promise<void>

  removeSkill: (id: string) => Promise<void>

  updateVacancy: (vacancy: VacancyDocument) => Promise<void>

  removeAllVacancies: () => Promise<void>

  startTestSetup: (id: string) => Promise<void>

  completeTestSetup: (id: string) => Promise<void>
}

export const REPOSITORY_TYPE = Symbol.for('Repository')
