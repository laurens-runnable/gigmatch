import { testDashboard as test } from '../fixtures'
import { expect } from '@playwright/test'

// noinspection JSUnusedLocalSymbols
test('Vacancies', async ({ page, loginPage, testSet }) => {
  await page.goto('/dashboard/vacancies')
  await expect(page).toHaveTitle(/Vacancies/)
})
