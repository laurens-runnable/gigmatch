<script setup>
const links = [
  { path: '/', text: 'Home' },
  { path: '/vacancies', text: 'Vacancies' },
]
</script>

<template>
  <v-app id="inspire">
    <v-app-bar flat>
      <v-container class="fill-height d-flex align-center">
        <v-avatar
          class="me-10 ms-4"
          color="grey-darken-1"
          size="32"
          title="Gigmatch"
        ></v-avatar>

        <NuxtLink v-for="link in links" :key="link.path" :href="link.path">
          <v-btn variant="text">
            {{ link.text }}
          </v-btn>
        </NuxtLink>

        <v-spacer />

        <gm-user-avatar name="Qwiff" />
      </v-container>
    </v-app-bar>

    <v-main class="bg-grey-lighten-3">
      <v-container>
        <v-row>
          <v-col>
            <v-sheet min-height="70vh" rounded="lg">
              <slot />
            </v-sheet>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>
