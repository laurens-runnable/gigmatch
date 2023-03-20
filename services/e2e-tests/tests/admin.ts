import fetch from 'node-fetch'

export async function fetchAdminAccessToken(baseURL: string): Promise<string> {
  const params = new URLSearchParams()
  params.append('username', 'admin1')
  params.append('password', 'admin1')
  params.append('grant_type', 'password')
  params.append('client_id', 'gigmatch-admin')
  params.append('client_secret', 'secret')

  const url = `${baseURL}/auth/realms/gigmatch/protocol/openid-connect/token`
  const response = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: params,
  })
  const tokens = await response.json()
  return tokens.access_token
}
