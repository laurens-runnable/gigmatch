// eslint-disable-next-line import/default
import avsc from 'avsc'
import OpenVacancySchema from './OpenVacancy.avsc.json'

// eslint-disable-next-line import/no-named-as-default-member
const { Type, types } = avsc

class LogicalDateType extends types.LogicalType {
  _toValue(val: any) {
    return val ? Math.round(val.getTime() / (3600 * 24 * 1000)) : undefined
  }
}

// @ts-ignore
export const OpenVacancy = Type.forSchema(OpenVacancySchema, {
  logicalTypes: { date: LogicalDateType },
})
