import { H3Event } from 'h3'
import { IronSessionData, getIronSession } from 'iron-session'
import jwtDecode from 'jwt-decode'

declare module 'iron-session' {
  interface IronSessionData {
    codeVerifier?: string
    accessToken?: string
    refreshToken?: string
    originalUrl?: string
  }
}

export async function useSession(event: H3Event): Promise<IronSessionData> {
  if (event.context.session) {
    return event.context.session
  }

  const {
    session: { cookieName, password, secure },
  } = useRuntimeConfig()

  const session = await getIronSession(event.node.req, event.node.res, {
    cookieName,
    password,
    cookieOptions: {
      secure,
      httpOnly: true,
      maxAge: undefined,
    },
  })
  event.context.session = session
  return session
}

export async function useJwt(event: H3Event): Promise<any> {
  const session = await useSession(event)
  if (!session.accessToken) {
    throw new Error('Access token not found')
  }
  return jwtDecode<any>(session.accessToken)
}
