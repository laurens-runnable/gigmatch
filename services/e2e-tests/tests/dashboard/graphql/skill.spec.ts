import { testDashboard as test } from '../../fixtures'
import { gql } from '@apollo/client/core'
import { expect } from '@playwright/test'

// noinspection JSUnusedLocalSymbols
test('skills() query', async ({ page, user, testSet, testSetup, graphql }) => {
  await testSetup.completion()

  const {
    data: { skills },
  } = await graphql.client.query({
    query: gql`
      query {
        skills {
          id
          name
          slug
        }
      }
    `,
  })

  expect(skills).toBeInstanceOf(Array)
  expect(skills.length).toBe(5)
})
