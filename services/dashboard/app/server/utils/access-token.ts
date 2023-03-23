import { H3Event } from 'h3'
import jwtDecode from 'jwt-decode'
import { useSession } from '~/server/utils/session'

export async function useAccessToken(event: H3Event): Promise<string | null> {
  const { authorization } = event.node.req.headers
  const matches = authorization?.match(/^Bearer\s+(.+)$/)
  if (matches?.length === 2) {
    return matches[1]
  }

  const session = await useSession(event)
  if (!session.accessToken) {
    return null
  }
  return session.accessToken
}

export async function useJwt(event: H3Event): Promise<any> {
  const accessToken = await useAccessToken(event)
  if (accessToken === null) {
    throw new Error('Access token not found')
  }
  return jwtDecode<any>(accessToken)
}
