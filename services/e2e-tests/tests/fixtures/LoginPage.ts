import { expect, Page } from '@playwright/test'

class LoginPage {
  readonly page: Page

  constructor(page: Page) {
    this.page = page
  }

  async login(path: string, username: string, password: string) {
    const page = this.page
    await page.goto(path)

    await expect(page).toHaveURL(/auth/)

    await page.locator('#username').fill(username)
    await page.locator('#password').fill(password)
    await page.getByRole('button').click()

    await expect(page).toHaveURL(new RegExp(path))
  }
}

export default LoginPage
