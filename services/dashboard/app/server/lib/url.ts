export function useAppUrl(path = ''): string {
  const {
    app: { baseURL },
  } = useRuntimeConfig()

  return `${baseURL}${path}`
}
