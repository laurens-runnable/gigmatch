<script setup>
import { useSeoMeta } from '#head'

definePageMeta({
  layout: 'index',
})
useSeoMeta({ title: 'Home' })
</script>

<template>
  <v-container>
    <v-row>
      <v-col>
        <h1>Home</h1>
      </v-col>
    </v-row>
  </v-container>
</template>
