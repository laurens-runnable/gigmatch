import { testDashboard as test } from '../../fixtures'
import { gql } from '@apollo/client/core'
import { expect } from '@playwright/test'
import { print } from 'graphql/index'

// noinspection JSUnusedLocalSymbols
test('currentUser() should return user', async ({
  page,
  loginPage,
  testSet,
  testSetup,
}) => {
  await testSetup.completion()

  const { request } = page.context()
  const query = await request.post('/dashboard/api/graphql', {
    data: {
      query: print(gql`
        query {
          currentUser {
            firstName
            lastName
          }
        }
      `),
    },
  })

  expect(query.status()).toBe(200)

  const { firstName, lastName } = (await query.json()).data.currentUser
  expect(firstName).toBe('Ricky')
  expect(lastName).toBe('Recruiter')
})
