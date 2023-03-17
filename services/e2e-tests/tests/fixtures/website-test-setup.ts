import { Client } from '@elastic/elasticsearch'
import { type APIRequestContext, expect } from '@playwright/test'
import { v4 as uuidv4 } from 'uuid'

let _client: Client

interface TestSetupDocument {
  readonly id: string
  readonly isActive: boolean
}

function getClient(url: string): Client {
  if (_client === undefined) {
    _client = new Client({ node: url })
  }
  return _client
}

async function testSetupSynchronization(
  id: string,
  timeout = 5000,
  pollInterval = 100
): Promise<void> {
  let timePassed = 0
  while (true) {
    const client = getClient('http://localhost:9200')
    const {
      hits: { hits },
    } = await client.search<TestSetupDocument>({
      index: 'test-setup',
      query: { match: { id } },
    })
    if (hits.length === 1 && hits[0]._source?.isActive === false) {
      await client.delete({ index: 'test-setup', id })
      break
    }

    if (timePassed >= timeout) {
      throw new Error('Test synchronization timeout')
    }

    await new Promise<void>((resolve) => {
      setTimeout(() => {
        timePassed += pollInterval
        resolve()
      }, pollInterval)
    })
  }
}

export default class WebsiteTestSetup {
  private readonly request: APIRequestContext
  private _id: string | null = uuidv4()

  constructor(request: APIRequestContext) {
    this.request = request
  }

  async start(): Promise<void> {
    const post = await this.request.post('/matches/actuator/test-setup-start', {
      data: { id: this._id },
    })
    expect(post.status()).toBe(204)
  }

  async completion(): Promise<void> {
    const post = await this.request.post(
      '/matches/actuator/test-setup-complete',
      { data: { id: this._id } }
    )
    expect(post.status()).toBe(204)
    await testSetupSynchronization(this._id as string)
    this._id = null
  }
}
