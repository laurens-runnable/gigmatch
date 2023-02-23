import { testDashboard as test } from '../fixtures'

// noinspection JSUnusedLocalSymbols
test('Logout', async ({ page, user, testSet }) => {
  await page.goto('/dashboard')

  await page.locator('.gm-user').click()
  await page.locator('.gm-logout').click()

  await page.waitForURL(/openid-connect\/logout/)
  await page.getByRole('button').click()

  await page.waitForURL(/openid-connect\/auth/)

  user.skipLogout()
})
