import { startAuthorizationFlow } from '~/server/lib/open-id'
import { useSession } from '~/server/lib/session'

export default defineEventHandler(async (event) => {
  const { req } = event.node
  if (req.url?.match(/^\/api\/authorize\/.*$/)) {
    return
  }

  const session = await useSession(event)
  if (!session.username) {
    await startAuthorizationFlow(event)
  }
})
