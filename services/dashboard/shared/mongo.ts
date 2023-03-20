export interface ExperienceItem {
  skillId: string
  experienceLevel: string
}

export interface VacancyDocument {
  id: string
  jobTitle: string
  start: Date
  end: Date
  rateAmount: number
  rateType: string
  deadline: Date
  userId: string
  isOpen: boolean
  experience: Array<ExperienceItem>
}

export interface SkillDocument {
  readonly id: string
  readonly name: string
  readonly slug: string
}
