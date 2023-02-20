<script setup>
import {
  ApolloClient,
  InMemoryCache,
  createHttpLink,
} from '@apollo/client/core'
import { DefaultApolloClient } from '@vue/apollo-composable'

const apolloClient = new ApolloClient({
  link: createHttpLink({
    uri: '/dashboard/api/graphql',
  }),
  cache: new InMemoryCache(),
})
provide(DefaultApolloClient, apolloClient)
</script>
<template>
  <div>
    <NuxtLayout>
      <NuxtPage />
    </NuxtLayout>
  </div>
</template>
