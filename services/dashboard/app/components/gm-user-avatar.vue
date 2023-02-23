<script setup lang="ts">
import { useQuery } from '@vue/apollo-composable'
import gql from 'graphql-tag'
import { UserDetails } from '~/server/graphql/user-details'
import { useAppUrl } from '~/server/lib/url'

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

const items = [
  {
    title: 'Logout',
    handle() {
      window.location.href = useAppUrl('/api/logout/start')
    },
  },
]
</script>
<template>
  <v-btn
    v:if="displayName"
    color="info"
    size="32"
    :title="displayName"
    class="gm-user"
  >
    <v-icon icon="mdi-account" />
    <v-menu activator="parent">
      <v-list>
        <v-list-item
          v-for="(item, index) in items"
          :key="index"
          :value="index"
          class="gm-logout"
          @click="item.handle"
        >
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
  </v-btn>
</template>
