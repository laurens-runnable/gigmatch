import { testDashboard as test } from '../../fixtures'
import { gql } from '@apollo/client/core'
import { Page, expect } from '@playwright/test'
import { print } from 'graphql/index'

async function queryActiveVacancies(page: Page) {
  const { request } = page.context()
  const query = await request.post('/dashboard/api/graphql', {
    data: {
      query: activeVacancies(),
    },
  })

  await expect(query.status()).toBe(200)

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
}) => {
  const vacancies = await queryActiveVacancies(page)
  expect(vacancies).toBeInstanceOf(Array)
  expect(vacancies.length).toBe(1)
})

function activeVacancies() {
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

// noinspection JSUnusedLocalSymbols
test('createVacancy() should return vacancy', async ({
  page,
  loginPage,
  testSet,
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

  await expect(mutation.status()).toBe(200)

  const response = await mutation.json()
  expect(response.errors).toBeUndefined()

  const vacancy = response.data.createVacancy
  expect(vacancy.id.length).toBeGreaterThan(0)
  expect(vacancy.name).toStrictEqual('Kotlin developer')

  const vacancies = await queryActiveVacancies(page)
  expect(vacancies.length).toBe(2)
})
