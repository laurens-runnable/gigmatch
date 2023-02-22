<script setup lang="ts">
import { useQuery } from '@vue/apollo-composable'
import gql from 'graphql-tag'
import { UserDetails } from '~/server/graphql/user-details'

const { result, loading, error } = useQuery(gql`
  query {
    currentUser {
      firstName
      lastName
    }
  }
`)

const displayName = computed<string | undefined>(() => {
  if (!loading.value && !error.value) {
    const userDetails: UserDetails | null = result.value.currentUser
    if (userDetails) {
      return `${userDetails.firstName} ${userDetails.lastName}`
    }
  }
})
</script>
<template>
  <v-avatar
    v:if="displayName"
    class="me-10 ms-4"
    size="32"
    icon="mdi-account"
    :title="displayName"
  />
</template>
