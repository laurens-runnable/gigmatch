// eslint-disable-next-line import/default
import avsc from 'avsc'
import CreateVacancySchema from './CreateVacancy.avsc.json'

// eslint-disable-next-line import/no-named-as-default-member
const { Type } = avsc

// @ts-ignore
export const CreateVacancy = Type.forSchema(CreateVacancySchema)
