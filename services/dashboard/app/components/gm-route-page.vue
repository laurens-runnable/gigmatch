<script setup lang="ts">
const { t } = useI18n()
const appTitle = t(`app.title`)

const routeTitle = useRouteTitle()

useSeoMeta({ title: `${routeTitle} - ${appTitle}` })
</script>

<template>
  <v-row>
    <v-col>
      <slot :route-title="routeTitle" :app-title="appTitle" />
    </v-col>
  </v-row>
</template>
