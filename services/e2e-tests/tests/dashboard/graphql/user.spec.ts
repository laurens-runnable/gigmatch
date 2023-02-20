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
            username
          }
        }
      `),
    },
  })

  expect(query.status()).toBe(200)

  const { username } = (await query.json()).data.currentUser
  expect(username).toBe('recruiter1')
})
