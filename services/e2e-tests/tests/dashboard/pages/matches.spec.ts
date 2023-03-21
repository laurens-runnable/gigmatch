import { testDashboard as test } from '../../fixtures'
import { expect } from '@playwright/test'

// noinspection JSUnusedLocalSymbols
test('Matches', async ({ page, user, testSet }) => {
  await page.goto('/dashboard/matches')
  await expect(page).toHaveTitle(/Matches/)
})
