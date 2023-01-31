import assert from 'assert'
import { H3Event, sendError } from 'h3'
import { BaseClient, Issuer, custom, generators } from 'openid-client'
import { useSession } from '~/server/lib/session'

custom.setHttpOptionsDefaults({
  timeout: 10000,
})

let issuer: Issuer

async function getOpenIdClient(): Promise<BaseClient> {
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
    app: { baseURL },
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
  const userInfo = await client.userinfo(tokenSet)

  const originalUrl = session.originalUrl ?? ''
  session.codeVerifier = undefined
  session.originalUrl = undefined

  const { access_token: accessToken, refresh_token: refreshToken } = tokenSet
  session.accessToken = accessToken
  session.refreshToken = refreshToken
  session.username = userInfo.name ?? userInfo.preferred_username
  await session.save()

  await sendRedirect(event, `${baseURL}${originalUrl}`)
}
