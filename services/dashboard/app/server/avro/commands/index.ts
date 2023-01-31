import CreateVacancySchema from './CreateVacancy.avsc.json'
import avsc from 'avsc'

const { Type } = avsc

// @ts-ignore
export const CreateVacancy = Type.forSchema(CreateVacancySchema)
