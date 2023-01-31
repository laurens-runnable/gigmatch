export type RetryConfig = {
  retries: number
  factor: number
  timeout: number
}

/**
 * Creates a RetryConfig from the environment.
 */
export function createRetryConfig(): RetryConfig {
  return {
    retries: Number(process.env.GIGMATCH_RETRY_RETRIES ?? 10),
    timeout: Number(process.env.GIGMATCH_RETRY_TIMEOUT ?? 1000),
    factor: Number(process.env.GIGMATCH_RETRY_FACTOR ?? 2),
  }
}
