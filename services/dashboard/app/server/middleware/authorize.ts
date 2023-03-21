import jwtDecode, { JwtPayload } from 'jwt-decode'

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
      try {
        await renewTokens(event)
      } catch (e) {
        // Tokens are invalid
        await startAuthorizationFlow(event)
      }
    }
  } else {
    await startAuthorizationFlow(event)
  }
})
