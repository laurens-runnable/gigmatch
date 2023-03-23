import jwtDecode, { JwtPayload } from 'jwt-decode'
import { useAccessToken } from '~/server/utils/access-token'

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

  const accessToken = await useAccessToken(event)
  if (accessToken !== null) {
    if (isExpired(accessToken)) {
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
