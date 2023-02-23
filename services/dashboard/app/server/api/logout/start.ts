import { startLogout } from '~/server/lib/open-id'

export default defineEventHandler(async (event) => {
  await startLogout(event)
})
