<script setup>
import { useQuery } from '@vue/apollo-composable'
import gql from 'graphql-tag'

const { result } = useQuery(gql`
  query {
    currentUser {
      username
    }
  }
`)
</script>
<template>
  <v-avatar
    class="me-10 ms-4"
    size="32"
    icon="mdi-account"
    :title="result?.currentUser.username"
  />
</template>
