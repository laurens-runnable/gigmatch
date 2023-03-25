<script setup lang="ts">
import GmMenuButton from '~/layouts/gm-menu-button.vue'

const isDrawerVisible = ref(true)

type NavItem = {
  id: string
  path: string
  icon: string
}

const navItems: NavItem[] = [
  {
    id: 'home',
    path: '/',
    icon: 'mdi-home',
  },
  {
    id: 'vacancies',
    path: '/vacancies',
    icon: 'mdi-view-list-outline',
  },
  {
    id: 'applications',
    icon: 'mdi-account-group',
    path: '/applications',
  },
  {
    id: 'matches',
    icon: 'mdi-bullseye-arrow',
    path: '/matches',
  },
]
</script>

<template>
  <v-app id="gigmatch">
    <v-navigation-drawer v-model="isDrawerVisible">
      <v-toolbar density="compact">
        <v-app-bar-nav-icon></v-app-bar-nav-icon>
        <v-toolbar-title>{{ $t('app.title') }}</v-toolbar-title>
        <gm-menu-button />
      </v-toolbar>
      <v-divider />
      <v-list density="compact" nav>
        <v-list-item
          v-for="item in navItems"
          :key="item.id"
          :prepend-icon="item.icon"
          :to="item.path"
          :title="$t(`nav.${item.id}`)"
        ></v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-main>
      <v-container fluid>
        <router-view />
      </v-container>
    </v-main>
  </v-app>
</template>

<style lang="scss">
.v-main {
  background-color: #f5f5f9;
}
</style>
