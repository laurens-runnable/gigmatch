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

const menuItems = [
  {
    title: 'Logout',
    handle() {
      window.location.href = useAppUrl('/api/logout/start')
    },
  },
]
</script>
<template>
  <v-btn v:if="displayName" color="white" block class="gm-user">
    <v-menu activator="parent">
      <v-list>
        <v-list-item
          v-for="(item, index) in menuItems"
          :key="index"
          :value="index"
          class="gm-logout"
          @click="item.handle"
        >
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
    <v-icon icon="mdi-account" size="1.25rem" start />
    {{ displayName }}
  </v-btn>
</template>
