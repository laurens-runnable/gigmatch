<script setup lang="ts">
definePageMeta({ layout: 'index' })
const title = inject('title')
</script>

<template>
  <gm-route-page>
    {{ title }}
  </gm-route-page>
</template>
