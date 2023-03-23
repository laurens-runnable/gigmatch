import { fetchAccessToken } from '../../shared/access-token'
import { LOGGER_TYPE, Logger } from '../logger'
import { type DataGenerator } from './index'
import { type NormalizedCacheObject } from '@apollo/client/cache/inmemory/types'
import {
  ApolloClient,
  InMemoryCache,
  createHttpLink,
  gql,
} from '@apollo/client/core'
import { setContext } from '@apollo/client/link/context'
import { faker } from '@faker-js/faker'
import { promises as fs } from 'fs'
import { inject, injectable } from 'inversify'
import fetch from 'node-fetch'
import path from 'path'

const CREATE_VACANCY = gql`
  mutation CreateVacancy(
    $jobTitle: String!
    $start: Date!
    $end: Date!
    $rateAmount: Int!
    $deadline: Date!
  ) {
    createVacancy(
      vacancy: {
        jobTitle: $jobTitle
        start: $start
        end: $end
        experience: [
          { skillId: "0cbfbd6a-8e4a-43cb-8aea-f75b71b32c35", level: SENIOR }
        ]
        rateAmount: $rateAmount
        rateType: HOURLY
        deadline: $deadline
      }
    ) {
      id
      jobTitle
      experience {
        skillId
        level
      }
      isOpen
    }
  }
`

function toDateString(date: Date): string {
  return date.toISOString().split('T')[0]
}

function addDays(date: Date, days: number): Date {
  const result = new Date(date)
  result.setDate(result.getDate() + days)
  return result
}

function randomInt(min: number, max: number): number {
  return min + Math.round(Math.random() * (max - min))
}

function randomStart(): Date {
  const days = randomInt(14, 90)
  return addDays(new Date(), days)
}

function generateVacancy(): any {
  const start = randomStart()
  const end = addDays(start, randomInt(200, 400))
  const deadline = addDays(start, -randomInt(0, 7))
  return {
    jobTitle: faker.name.jobTitle(),
    start: toDateString(start),
    end: toDateString(end),
    rateAmount: randomInt(60, 120),
    rateType: 'HOURLY',
    deadline: toDateString(deadline),
  }
}

@injectable()
export class RandomDataGenerator implements DataGenerator {
  private readonly _logger: Logger
  private _client?: ApolloClient<NormalizedCacheObject>

  constructor(@inject(LOGGER_TYPE) logger: Logger) {
    this._logger = logger
  }

  async init(baseUrl: string): Promise<void> {
    const csv = await fs.readFile(
      path.join(__dirname, '../../shared/skills.csv')
    )
    await this._importSkills(baseUrl, csv)

    await this._createApolloClient(baseUrl)
  }

  private async _createApolloClient(baseUrl: string): Promise<string> {
    const accessToken = await fetchAccessToken(
      baseUrl,
      'recruiter1',
      'recruiter1'
    )

    const uri = `${baseUrl}/dashboard/api/graphql`
    const httpLink = createHttpLink({ uri })

    const authLink = setContext((_, { headers }) => {
      return {
        headers: {
          ...headers,
          authorization: `Bearer ${accessToken}`,
        },
      }
    })
    this._client = new ApolloClient({
      link: authLink.concat(httpLink),
      cache: new InMemoryCache(),
    })
    return accessToken
  }

  private async _importSkills(baseUrl: string, csv: Buffer): Promise<void> {
    const accessToken = await fetchAccessToken(baseUrl, 'admin1', 'admin1')
    this._logger.info('Importing skills')
    await fetch(`${baseUrl}/matches/api/v1/reference-data/skills`, {
      method: 'POST',
      headers: {
        'Content-Type': 'text/csv',
        Authorization: `Bearer ${accessToken}`,
      },
      body: csv.toString(),
    })
  }

  async generateVacancies(amount: number): Promise<void> {
    const tasks: Array<Promise<void>> = []
    for (let i = 0; i < amount; i++) {
      tasks.push(this._createVacancy())
    }
    await Promise.all(tasks)
  }

  async _createVacancy(): Promise<void> {
    await this._client?.mutate({
      mutation: CREATE_VACANCY,
      variables: generateVacancy(),
    })
  }
}
