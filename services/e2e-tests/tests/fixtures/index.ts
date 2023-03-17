import DashboardTestSetup from './dashboard-test-setup'
import TestSet from './test-set'
import User from './user'
import WebsiteTestSetup from './website-test-setup'
import { test as base } from '@playwright/test'

interface Fixture {
  user: User
  testSet: TestSet
  testSetup: DashboardTestSetup
}

const testSet = async ({ request }: any, use: any): Promise<void> => {
  const testSet = new TestSet(request)
  await testSet.reset()
  await use(testSet)
}

export const testDashboard = base.extend<Fixture>({
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
  testSetup: async ({ request }: any, use: any): Promise<void> => {
    const testSetup = new DashboardTestSetup(request)
    await testSetup.start()
    await use(testSetup)
  },
})

export const testWebsite = base.extend<Fixture>({
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
  testSetup: async ({ request }: any, use: any): Promise<void> => {
    const testSetup = new WebsiteTestSetup(request)
    await testSetup.start()
    await use(testSetup)
  },
})
