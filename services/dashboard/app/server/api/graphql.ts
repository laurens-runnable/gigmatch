import { createSchema, createYoga } from 'graphql-yoga'
import { H3Event, H3EventContext, send, setHeaders } from 'h3'
import { resolvers, typeDefs } from '../graphql/schema'

const yoga = createYoga<H3EventContext>({
  graphqlEndpoint: '/api/graphql',
  schema: createSchema({
    typeDefs,
    resolvers,
  }),
})

async function queryYoga(event: H3Event): Promise<Response> {
  const {
    req: { url, method, headers },
  } = event.node
  const body = await readRawBody(event)

  return yoga.fetch(url!, { method, headers, body }, { event })
}

async function sendResponse(response: Response, event: H3Event) {
  const headers: Record<string, string> = {}
  response.headers.forEach((value, key) => {
    headers[key] = value
  })
  setHeaders(event, headers)

  const body = await response.text()
  await send(event, body)
}

export default defineEventHandler(async (event: H3Event) => {
  const response = await queryYoga(event)

  await sendResponse(response, event)
})
