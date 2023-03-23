import { testDashboard as test } from '../../fixtures'
import { gql } from '@apollo/client/core'
import { expect } from '@playwright/test'

const CREATE_VACANCY = gql`
  mutation {
    createVacancy(
      vacancy: {
        jobTitle: "Kotlin developer"
        start: "2023-07-01"
        end: "2023-12-31"
        experience: [
          { skillId: "0cbfbd6a-8e4a-43cb-8aea-f75b71b32c35", level: SENIOR }
        ]
        rateAmount: 100
        rateType: HOURLY
        deadline: "2023-06-15"
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

const OPEN_VACANCY = gql`
  mutation OpenVacancy($id: UUID!) {
    openVacancy(id: $id)
  }
`

// noinspection JSUnusedLocalSymbols
test('vacancy creation', async ({
  page,
  user,
  testSet,
  testSetup,
  graphql,
}) => {
  const {
    data: { createVacancy },
  } = await graphql.client.mutate({
    mutation: CREATE_VACANCY,
  })

  const { id } = createVacancy
  expect(id.length).toBeGreaterThan(0)
  expect(createVacancy.experience.length).toBe(1)
  expect(createVacancy.jobTitle).toStrictEqual('Kotlin developer')
  expect(createVacancy.isOpen).toStrictEqual(false)

  await graphql.client.mutate({
    mutation: OPEN_VACANCY,
    variables: { id },
  })

  await testSetup.completion()

  const {
    data: { vacancies },
  } = await graphql.client.query({
    query: gql`
      query FindVacancy($id: UUID!, $type: VacancyType) {
        vacancies(filter: { id: [$id], type: $type }) {
          id
          isOpen
        }
      }
    `,
    variables: { id, type: 'OPEN' },
  })

  expect(vacancies.length).toBe(1)
  const [vacancy] = vacancies
  expect(vacancy.isOpen).toStrictEqual(true)
})
