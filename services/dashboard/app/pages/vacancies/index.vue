<script setup>
import { useSeoMeta } from '#head'

definePageMeta({
  layout: 'index',
})

useSeoMeta({ title: 'Vacancies' })
</script>

<template>
  <v-container>
    <v-row>
      <v-col>
        <h1>Vacancies</h1>
      </v-col>
    </v-row>
  </v-container>
</template>
