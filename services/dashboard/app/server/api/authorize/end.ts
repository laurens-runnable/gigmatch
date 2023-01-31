import { endAuthorizationFlow } from '~/server/lib/open-id'

export default defineEventHandler(async (event) => {
  await endAuthorizationFlow(event)
})
