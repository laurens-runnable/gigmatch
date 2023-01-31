import { fetchSkills } from './skills'
import {
  CreateVacancyParams,
  createVacancy,
  fetchActiveVacancies,
} from './vacancies'
import { DateResolver, UUIDResolver } from 'graphql-scalars'
import { H3Event } from 'h3'

export const typeDefs = /* GraphQL */ `
  scalar Date
  scalar UUID

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
    allSkills: () => fetchSkills(),
    activeVacancies: () => fetchActiveVacancies(),
  },
  Mutation: {
    createVacancy: (
      parent: unknown,
      params: CreateVacancyParams,
      context: H3EventContext
    ) => {
      return createVacancy(params, context.event)
    },
  },
}
