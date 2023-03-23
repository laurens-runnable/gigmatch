export interface ExperienceItem {
  readonly skillId: string
  readonly experienceLevel: string
}

export interface VacancyDocument {
  readonly id: string
  readonly jobTitle: string
  readonly start: Date
  readonly end: Date
  readonly rateAmount: number
  readonly rateType: string
  readonly deadline: Date
  readonly experience: Array<ExperienceItem>
  readonly isPending: boolean
  readonly isOpen: boolean
  readonly userId: string
}

export interface SkillDocument {
  readonly id: string
  readonly name: string
  readonly slug: string
}
