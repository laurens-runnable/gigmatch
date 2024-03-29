import { testWebsite as test } from '../fixtures'
import { expect } from '@playwright/test'

// noinspection JSUnusedLocalSymbols
test('Home', async ({ page, user, testSet, testSetup }) => {
  await testSetup.completion()

  const response = await page.goto('/website')
  expect(response?.ok()).toBeTruthy()
  await expect(page).toHaveTitle(/Gigmatch/)
})
