import { testWebsite as test } from '../fixtures'
import { expect } from '@playwright/test'

// noinspection JSUnusedLocalSymbols
test('Vacancies', async ({ page, user, testSet, testSetup }) => {
  await testSetup.completion()

  const response = await page.goto('/website/vacancies')
  expect(response?.ok()).toBeTruthy()
  await expect(page).toHaveTitle(/Gigmatch/)
})
