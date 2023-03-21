<script setup>
definePageMeta({ layout: 'index' })
const title = useRouteTitle()
</script>

<template>
  <v-container>
    <v-row>
      <v-col>
        <h1>{{ title }}</h1>
      </v-col>
    </v-row>
  </v-container>
</template>
