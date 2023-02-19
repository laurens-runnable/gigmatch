import { createSchema, createYoga } from 'graphql-yoga'
import { H3EventContext, send, setHeaders } from 'h3'
import { resolvers, typeDefs } from '../graphql/schema'

const yoga = createYoga<H3EventContext>({
  graphqlEndpoint: '/api/graphql',
  schema: createSchema({
    typeDefs,
    resolvers,
  }),
})

export default defineEventHandler(async (event) => {
  const { req } = event.node
  const body = await readRawBody(event)
  const response = await yoga.fetch(
    req.url!,
    {
      method: req.method,
      headers: req.headers,
      body,
    },
    {
      event,
    }
  )

  const headers: Record<string, string> = {}
  response.headers.forEach((value, key) => {
    headers[key] = value
  })
  setHeaders(event, headers)
  await send(event, response.body)
})
