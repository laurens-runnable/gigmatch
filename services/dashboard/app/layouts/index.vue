<script setup lang="ts">
const drawer = ref(true)

type NavItem = {
  id: string
  path: string
  icon: string
}

const items: NavItem[] = [
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
    <v-navigation-drawer v-model="drawer">
      <v-list-item>Gigmatch</v-list-item>
      <v-divider />
      <v-list density="compact" nav>
        <v-list-item
          v-for="item in items"
          :key="item.id"
          :prepend-icon="item.icon"
          :to="item.path"
          :title="$t(`nav.${item.id}`)"
        ></v-list-item>
      </v-list>
      <template #append>
        <div class="pa-2">
          <gm-user-button />
        </div>
      </template>
    </v-navigation-drawer>

    <v-main>
      <slot />
    </v-main>
  </v-app>
</template>

<style lang="scss">
.v-main {
  background-color: #f5f5f9;
}
</style>
