import { testWebsite as test } from '../fixtures'
import { expect } from '@playwright/test'

// noinspection JSUnusedLocalSymbols
test('Home', async ({ page, loginPage, testSet }) => {
  const response = await page.goto('/website')
  expect(response?.ok()).toBeTruthy()
  await expect(page).toHaveTitle(/Gigmatch/)
})
