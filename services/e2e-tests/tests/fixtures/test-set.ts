import { type APIRequestContext, expect } from '@playwright/test'

export default class TestSet {
  readonly request: APIRequestContext

  constructor(request: APIRequestContext) {
    this.request = request
  }

  async reset(): Promise<void> {
    const post = await this.request.post('/matches/actuator/test-set')
    expect(post.status()).toBe(204)
  }
}
