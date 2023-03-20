import { testDashboard as test } from '../../fixtures'
import { gql } from '@apollo/client/core'
import { expect } from '@playwright/test'

// noinspection JSUnusedLocalSymbols
test('currentUser() query', async ({
  page,
  user,
  testSet,
  testSetup,
  graphql,
}) => {
  await testSetup.completion()

  const {
    data: { currentUser },
  } = await graphql.client.query({
    query: gql`
      query {
        currentUser {
          firstName
          lastName
        }
      }
    `,
  })

  const { firstName, lastName } = currentUser
  expect(firstName).toBe('Ricky')
  expect(lastName).toBe('Recruiter')
})
