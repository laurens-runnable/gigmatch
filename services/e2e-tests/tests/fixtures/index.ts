import TestSet from './test-set'
import TestSetup from './test-setup'
import User from './user'
import { test as base } from '@playwright/test'

export interface LoginFixture {
  user: User
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
  user: async ({ page, request }, use) => {
    const user = new User(page)
    await user.login('/dashboard', 'recruiter1', 'recruiter1')
    try {
      await use(user)
    } finally {
      await user.logout()
    }
  },
  testSet,
  testSetup,
})

export const testWebsite = base.extend<LoginFixture>({
  user: async ({ page, request }, use) => {
    const user = new User(page)
    await user.login('/website', 'candidate1', 'candidate1')
    try {
      await use(user)
    } finally {
      await user.logout()
    }
  },
  testSet,
  testSetup,
})
