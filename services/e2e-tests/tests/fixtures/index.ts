import LoginPage from './LoginPage'
import TestSet from './TestSet'
import { test as base } from '@playwright/test'

export type LoginFixture = {
  loginPage: LoginPage
  testSet: TestSet
}

export const test = base.extend<LoginFixture>({
  loginPage: async ({ page, request }, use) => {
    const loginPage = new LoginPage(page)
    await loginPage.login('user1', 'user1')
    await use(loginPage)
  },

  testSet: async ({ request }, use) => {
    const testSet = new TestSet(request)
    await testSet.reset()
    await use(testSet)
  },
})
