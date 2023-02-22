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
    name: String!
    start: Date
  }

  type Query {
    currentUser: UserDetails!
    allSkills: [Skill]!
    activeVacancies: [Vacancy]!
  }

  type Mutation {
    createVacancy(name: String!, start: Date): Vacancy
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
