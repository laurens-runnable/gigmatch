import { testDashboard as test } from '../../fixtures'
import { expect } from '@playwright/test'

// noinspection JSUnusedLocalSymbols
test('Home', async ({ page, user, testSet }) => {
  await page.goto('/dashboard')
  await expect(page).toHaveTitle(/Home/)
})
