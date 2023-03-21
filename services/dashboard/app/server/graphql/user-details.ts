import KcAdminClient from '@keycloak/keycloak-admin-client'
import { H3Event } from 'h3'

export interface UserDetails {
  firstName?: string
  lastName?: string
}

async function createKeycloakClient(): Promise<KcAdminClient> {
  const {
    keycloak: { url: baseUrl, username, password, realm: realmName, clientId },
  } = useRuntimeConfig()

  const client = new KcAdminClient({ baseUrl, realmName: 'master' })
  await client.auth({ username, password, clientId, grantType: 'password' })
  client.setConfig({ realmName })
  return client
}

async function findUserDetails(id: string): Promise<UserDetails> {
  const client = await createKeycloakClient()
  const user = await client.users.findOne({ id })
  if (user !== undefined) {
    const { firstName, lastName } = user
    return {
      firstName,
      lastName,
    }
  } else {
    throw new Error(`User not found '${id}'`)
  }
}

export async function queryCurrentUserDetails(
  event: H3Event
): Promise<UserDetails> {
  const jwt = await useJwt(event)
  return await findUserDetails(jwt.sub)
}
