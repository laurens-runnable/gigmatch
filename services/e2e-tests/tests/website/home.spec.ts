import { testWebsite as test } from '../fixtures'
import { expect } from '@playwright/test'

// noinspection JSUnusedLocalSymbols
test('Home', async ({ page, loginPage, testSet }) => {
  await page.goto('/website')
  await expect(page).toHaveTitle(/Gigmatch/)
})
