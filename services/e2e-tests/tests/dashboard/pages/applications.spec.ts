import { testDashboard as test } from '../../fixtures'
import { expect } from '@playwright/test'

// noinspection JSUnusedLocalSymbols
test('Applications', async ({ page, user, testSet }) => {
  await page.goto('/dashboard/applications')
  await expect(page).toHaveTitle(/Applications/)
})
