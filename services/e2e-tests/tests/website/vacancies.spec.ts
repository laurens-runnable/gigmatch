import { testWebsite as test } from '../fixtures'
import { expect } from '@playwright/test'

// noinspection JSUnusedLocalSymbols
test.skip('Vacancies', async ({ page, user, testSet, testSetup }) => {
  await testSetup.completion()

  const response = await page.goto('/website/vacancies')
  expect(response?.ok()).toBeTruthy()

  const items = await page.getByTestId('vacancies').locator('li').all()
  expect(items.length).toBe(1)
  expect(await items[0].innerText()).toMatch(/Java developer/)
})
