import LoginPage from './login-page'
import TestSet from './test-set'
import TestSetup from './test-setup'
import { test as base } from '@playwright/test'

export interface LoginFixture {
  loginPage: LoginPage
  testSet: TestSet
  testSetup: TestSetup
}

const testSet = async ({ request }: any, use: any): Promise<void> => {
  const testSet = new TestSet(request)
  await testSet.reset()
  await use(testSet)
}

const testSetup = async ({ request }: any, use: any): Promise<void> => {
  const testSetup = new TestSetup(request)
  await testSetup.start()
  await use(testSetup)
}

export const testDashboard = base.extend<LoginFixture>({
  loginPage: async ({ page, request }, use) => {
    const loginPage = new LoginPage(page)
    await loginPage.login('/dashboard', 'recruiter1', 'recruiter1')
    await use(loginPage)
  },
  testSet,
  testSetup,
})

export const testWebsite = base.extend<LoginFixture>({
  loginPage: async ({ page, request }, use) => {
    const loginPage = new LoginPage(page)
    await loginPage.login('/website', 'candidate1', 'candidate1')
    await use(loginPage)
  },
  testSet,
  testSetup,
})
