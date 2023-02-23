import { type Page, expect } from '@playwright/test'

export default class User {
  readonly page: Page
  private _skipLogout = false

  constructor(page: Page) {
    this.page = page
  }

  async login(path: string, username: string, password: string): Promise<void> {
    const page = this.page
    const response = await page.goto(path)

    expect(response?.status()).toBe(200)
    await expect(page).toHaveURL(/auth/)

    await page.locator('#username').fill(username)
    await page.locator('#password').fill(password)
    await page.getByRole('button').click()

    await expect(page).toHaveURL(new RegExp(path))
  }

  async logout(): Promise<void> {
    if (this._skipLogout) {
      return
    }

    const page = this.page
    const response = await page.goto(
      '/auth/realms/gigmatch/protocol/openid-connect/logout'
    )
    expect(response?.status()).toBe(200)

    await page.getByRole('button').click()

    await expect(page).toHaveURL(/logout-confirm/)
  }

  skipLogout(): void {
    this._skipLogout = true
  }
}
