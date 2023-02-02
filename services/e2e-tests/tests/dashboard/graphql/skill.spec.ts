import { test } from "../../fixtures";
import { gql } from "@apollo/client/core";
import { expect } from "@playwright/test";
import { print } from "graphql/index";


// noinspection JSUnusedLocalSymbols
test('allSkills() should return skills', async ({
  page,
  loginPage,
  testSet,
}) => {
  const { request } = page.context()
  const query = await request.post('/dashboard/api/graphql', {
    data: {
      query: print(gql`
        query {
          allSkills {
            id
            name
            slug
          }
        }
      `),
    },
  })

  await expect(query.status()).toBe(200)

  const skills = (await query.json()).data.allSkills
  expect(skills).toBeInstanceOf(Array)
  expect(skills.length).toBe(4)
})