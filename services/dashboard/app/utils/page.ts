import { useRouter } from 'vue-router'

export function useRouteTitle() {
  const router = useRouter()
  const { path } = router.currentRoute.value

  const { t } = useI18n()
  const title = t(`route.${path}.title`)

  useSeoMeta({ title })

  return title
}
