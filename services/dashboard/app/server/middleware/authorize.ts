import jwtDecode, { JwtPayload } from 'jwt-decode'
import { renewTokens, startAuthorizationFlow } from '~/server/lib/open-id'
import { useSession } from '~/server/lib/session'

function isExpired(accessToken: string) {
  const jwt = jwtDecode<JwtPayload>(accessToken)
  const expiration = jwt.exp as number
  const now = new Date().getTime() / 1000
  return now > expiration
}

export default defineEventHandler(async (event) => {
  const { req } = event.node
  if (req.url?.match(/^\/api\/authorize\/.*$/)) {
    return
  }

  const session = await useSession(event)
  if (session.accessToken) {
    if (isExpired(session.accessToken)) {
      await renewTokens(event)
    }
  } else {
    await startAuthorizationFlow(event)
  }
})
