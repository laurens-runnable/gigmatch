import { APIRequestContext, expect } from '@playwright/test'

class TestSet {
  readonly request: APIRequestContext

  constructor(request: APIRequestContext) {
    this.request = request
  }

  async reset() {
    const post = await this.request.post('/matches/actuator/test-set')
    expect(post.status()).toBe(204)
  }
}

export default TestSet
