import 'reflect-metadata'

export type Skill = {
  readonly id: string
  readonly name: string
  readonly slug: string
}

export type Vacancy = {
  id: string
  name: string
  start: Date
}

export interface Repository {
  open(): Promise<void>

  close(): Promise<void>

  updateSkill(skill: Skill): Promise<void>

  removeSkill(id: string): Promise<void>

  updateVacancy(vacancy: Vacancy): Promise<void>

  removeAllVacancies(): Promise<void>
}

export const REPOSITORY_TYPE = Symbol.for('Repository')
