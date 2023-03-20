import { type NormalizedCacheObject } from '@apollo/client/cache/inmemory/types'
import {
  ApolloClient,
  InMemoryCache,
  createHttpLink,
} from '@apollo/client/core'
import { type Page } from '@playwright/test'
import fetch, { type Response } from 'node-fetch'

/**
 * Provides a customized Apollo client for GraphQL operations in tests.
 *
 * This implementation simulates session login, by populating the `cookie`
 * request header with cookies from the Page context.
 */
export default class GraphQLContext {
  private readonly page: Page
  readonly client: ApolloClient<NormalizedCacheObject>

  constructor(page: Page, uri: string) {
    this.page = page
    this.client = new ApolloClient({
      link: createHttpLink({
        uri,
        fetch: this._fetchWithCookies.bind(this),
      }),
      cache: new InMemoryCache(),
    })
  }

  async _fetchWithCookies(uri: string, options: any): Promise<Response> {
    const cookies = await this.page.context().cookies()
    const cookieHeader = cookies.map((x) => `${x.name}=${x.value}`).join('; ')

    options.headers = {
      ...options.headers,
      cookie: cookieHeader,
    }
    return await fetch(uri, options)
  }
}
