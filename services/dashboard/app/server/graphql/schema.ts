import { DateResolver, UUIDResolver } from 'graphql-scalars'
import { H3Event } from 'h3'
import { fetchSkills } from './skills'
import { fetchCurrentUserDetails } from './user-details'
import { createVacancy, fetchActiveVacancies } from './vacancies'

export const typeDefs = /* GraphQL */ `
  scalar Date
  scalar UUID

  enum RateType {
    HOURLY
    DAILY
    FIXED
  }

  enum ExperienceLevel {
    JUNIOR
    MEDIOR
    SENIOR
  }

  type UserDetails {
    firstName: String
    lastName: String
  }

  type Skill {
    id: UUID!
    name: String!
    slug: String!
  }

  type Vacancy {
    id: UUID!
    jobTitle: String!
    start: Date!
    end: Date!
    rateAmount: Int!
    rateType: RateType!
    deadline: Date!
  }

  type Query {
    currentUser: UserDetails!
    allSkills: [Skill]!
    activeVacancies: [Vacancy]!
  }

  input ExperienceInput {
    skillId: UUID!
    level: ExperienceLevel!
  }

  input VacancyInput {
    jobTitle: String!
    start: Date!
    end: Date!
    experience: [ExperienceInput!]!
    rateAmount: Int!
    rateType: RateType!
    deadline: Date!
  }

  type Mutation {
    createVacancy(vacancy: VacancyInput!): Vacancy
  }
`

interface H3EventContext {
  event: H3Event
}

export const resolvers = {
  UUID: UUIDResolver,
  Date: DateResolver,
  Query: {
    currentUser: (_: unknown, __: unknown, context: H3EventContext) =>
      fetchCurrentUserDetails(context.event),
    allSkills: () => fetchSkills(),
    activeVacancies: () => fetchActiveVacancies(),
  },
  Mutation: {
    createVacancy: (_: unknown, {vacancy}: any, context: H3EventContext) => {
      return createVacancy(vacancy, context.event)
    },
  },
}
