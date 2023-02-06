import { testDashboard as test } from '../../fixtures'
import { gql } from '@apollo/client/core'
import { type Page, expect } from '@playwright/test'
import { print } from 'graphql/index'

function activeVacancies(): string {
  return print(gql`
    query {
      activeVacancies {
        id
        name
        start
      }
    }
  `)
}

async function queryActiveVacancies(page: Page): Promise<any> {
  const { request } = page.context()
  const query = await request.post('/dashboard/api/graphql', {
    data: {
      query: activeVacancies(),
    },
  })

  expect(query.status()).toBe(200)

  const response = await query.json()
  expect(response.errors).toBeUndefined()

  const vacancies = (await query.json()).data.activeVacancies
  expect(vacancies).toBeInstanceOf(Array)
  return vacancies
}
// noinspection JSUnusedLocalSymbols

test('activeVacancies() should return vacancies', async ({
  page,
  loginPage,
  testSet,
  testSetup,
}) => {
  await testSetup.completion()

  const vacancies = await queryActiveVacancies(page)
  expect(vacancies.length).toBe(1)
})

// noinspection JSUnusedLocalSymbols
test('createVacancy() should return vacancy', async ({
  page,
  loginPage,
  testSet,
  testSetup,
}) => {
  const { request } = page.context()
  const mutation = await request.post('/dashboard/api/graphql', {
    data: {
      query: print(gql`
        mutation {
          createVacancy(name: "Kotlin developer", start: "2023-07-01") {
            id
            name
            start
          }
        }
      `),
    },
  })

  expect(mutation.status()).toBe(200)

  const response = await mutation.json()
  expect(response.errors).toBeUndefined()

  const vacancy = response.data.createVacancy
  expect(vacancy.id.length).toBeGreaterThan(0)
  expect(vacancy.name).toStrictEqual('Kotlin developer')

  await testSetup.completion()

  const vacancies = await queryActiveVacancies(page)
  expect(vacancies.length).toBe(2)
})
