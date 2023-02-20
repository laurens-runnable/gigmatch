import { H3Event } from 'h3'
import { useSession } from '~/server/lib/session'

export interface User {
  username: string
}

export async function fetchCurrentUser(event: H3Event): Promise<User> {
  const session = await useSession(event)

  return {
    username: session.username,
  }
}
