import { endLogout } from '~/server/lib/open-id'

export default defineEventHandler(async (event) => {
  await endLogout(event)
})
