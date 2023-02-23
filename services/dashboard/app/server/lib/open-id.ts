import { H3Event } from 'h3'
import { Client, Issuer, custom, generators } from 'openid-client'
import { useSession } from '~/server/lib/session'
import { useAppUrl } from '~/server/lib/url'

custom.setHttpOptionsDefaults({
  timeout: 10000,
})

let issuer: Issuer

async function getOpenIdClient(): Promise<Client> {
  const { openId } = useRuntimeConfig()

  if (!issuer) {
    const { discoveryUrl } = openId
    issuer = await Issuer.discover(discoveryUrl)
  }

  const { clientId, clientSecret } = openId
  return new issuer.Client({
    client_id: clientId,
    client_secret: clientSecret,
  })
}

export async function startAuthorizationFlow(event: H3Event) {
  const {
    openId: { redirectUri },
  } = useRuntimeConfig()

  const codeVerifier = generators.codeVerifier()
  const client = await getOpenIdClient()
  const url = client.authorizationUrl({
    redirect_uri: redirectUri,
    response_type: 'code',
    response_mode: 'form_post',
    prompt: 'login',
    code_challenge_method: 'S256',
    code_challenge: generators.codeChallenge(codeVerifier),
  })
  const session = await useSession(event)
  session.codeVerifier = codeVerifier
  session.originalUrl = event.node.req.url
  await session.save()

  await sendRedirect(event, url)
}

export async function endAuthorizationFlow(event: H3Event) {
  if (event.node.req.method !== 'POST') {
    await sendNoContent(event, 400)
    return
  }

  const {
    openId: { redirectUri },
  } = useRuntimeConfig()

  const session = await useSession(event)

  const codeVerifier = session.codeVerifier
  if (!codeVerifier) {
    await sendNoContent(event, 401)
    return
  }

  const client = await getOpenIdClient()
  const params = await readBody(event)
  const tokenSet = await client.callback(redirectUri, params, {
    code_verifier: codeVerifier,
  })

  const originalUrl = session.originalUrl ?? ''
  session.codeVerifier = undefined
  session.originalUrl = undefined

  const { access_token: accessToken, refresh_token: refreshToken } = tokenSet
  session.accessToken = accessToken
  session.refreshToken = refreshToken
  await session.save()

  await sendRedirect(event, useAppUrl(originalUrl))
}

export async function renewTokens(event: H3Event) {
  const client = await getOpenIdClient()
  const session = await useSession(event)

  const tokenSet = await client.refresh(session.refreshToken as string)
  const { access_token: accessToken, refresh_token: refreshToken } = tokenSet
  session.accessToken = accessToken
  session.refreshToken = refreshToken
  await session.save()
}

export async function startLogout(event: H3Event) {
  const client = await getOpenIdClient()
  const url = client.endSessionUrl({
    post_logout_redirect_uri: useAppUrl('/api/logout/end'),
  })
  await sendRedirect(event, url)
}

export async function endLogout(event: H3Event) {
  const session = await useSession(event)
  session.accessToken = undefined
  session.refreshToken = undefined
  await session.save()

  await sendRedirect(event, useAppUrl())
}
