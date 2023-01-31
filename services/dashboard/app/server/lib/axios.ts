import axios from 'axios'
import axiosRetry from 'axios-retry'
import { H3Event } from 'h3'
import { useSession } from '~/server/lib/session'

export async function createMatchServiceClient(event: H3Event) {
  const {
    matchService: { baseUrl, timeout, retries },
  } = useRuntimeConfig()

  const session = await useSession(event)

  const instance = axios.create({
    baseURL: baseUrl,
    timeout,
    headers: {
      'Content-Type': 'application/avro',
      Authorization: `Bearer ${session.accessToken}`,
    },
  })

  axiosRetry(instance, { retries })

  return instance
}
