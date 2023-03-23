import { DateResolver, UUIDResolver } from 'graphql-scalars'
import { H3Event } from 'h3'
import { querySkills } from './skills'
import { queryCurrentUserDetails } from './user-details'
import {
  VacancyFilterInput,
  createVacancy,
  openVacancy,
  queryVacancies,
} from './vacancies'

export const typeDefs = /* GraphQL */ `
  scalar Date
  scalar UUID
  scalar Void

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

  type Experience {
    skillId: UUID!
    level: ExperienceLevel!
  }

  type Vacancy {
    id: UUID!
    jobTitle: String!
    start: Date!
    end: Date!
    experience: [Experience!]!
    rateAmount: Int!
    rateType: RateType!
    deadline: Date!
    isOpen: Boolean!
  }

  enum VacancyType {
    OPEN
    CLOSED
    PENDING
  }

  input VacancyFilterInput {
    id: [UUID!]
    type: VacancyType
  }

  type Query {
    currentUser: UserDetails!
    skills: [Skill]!
    vacancies(filter: VacancyFilterInput): [Vacancy]!
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
    openVacancy(id: UUID!): Void
  }
`

interface H3EventContext {
  event: H3Event
}

const QUERIES = {
  currentUser: (_: unknown, __: unknown, context: H3EventContext) =>
    queryCurrentUserDetails(context.event),

  skills: () => querySkills(),

  vacancies: (
    _: unknown,
    { filter }: { filter: VacancyFilterInput },
    context: H3EventContext
  ) => queryVacancies(filter, context.event),
}

const MUTATIONS = {
  createVacancy: (_: unknown, { vacancy }: any, context: H3EventContext) =>
    createVacancy(vacancy, context.event),

  openVacancy: (_: unknown, { id }: { id: string }, context: H3EventContext) =>
    openVacancy(id, context.event),
}

export const resolvers = {
  UUID: UUIDResolver,
  Date: DateResolver,
  Query: QUERIES,
  Mutation: MUTATIONS,
}
