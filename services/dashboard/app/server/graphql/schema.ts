import { DateResolver, UUIDResolver } from 'graphql-scalars'
import { H3Event } from 'h3'
import { fetchSkills } from './skills'
import { fetchCurrentUserDetails } from './user-details'
import {
  CreateVacancyParams,
  createVacancy,
  fetchActiveVacancies,
} from './vacancies'

export const typeDefs = /* GraphQL */ `
  scalar Date
  scalar UUID

  enum RateType {
    HOURLY
    DAILY
    FIXED
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
    listed: Boolean!
  }

  type Query {
    currentUser: UserDetails!
    allSkills: [Skill]!
    activeVacancies: [Vacancy]!
  }

  type Mutation {
    createVacancy(
      jobTitle: String!
      skillId: String!
      start: Date!
      end: Date!
      rateAmount: Int!
      rateType: RateType!
      deadline: Date!
      listed: Boolean!
    ): Vacancy
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
    createVacancy: (
      _: unknown,
      params: CreateVacancyParams,
      context: H3EventContext
    ) => {
      return createVacancy(params, context.event)
    },
  },
}
