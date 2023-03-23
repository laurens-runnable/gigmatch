<script setup lang="ts">
import GmVacanciesTable from './gm-vacancies-table.vue'

definePageMeta({ layout: 'index' })

interface TabItem {
  value: string
  label: string
}

const vacancyTypes = ['OPEN', 'CLOSED', 'PENDING']
const { t } = useI18n()
const tabItems: TabItem[] = vacancyTypes.map((option) => ({
  value: option,
  label: t(`vacancies.types.${option}`),
}))

const filter = ref('open')
</script>

<template>
  <gm-route-page v-slot="{ routeTitle }">
    <v-card>
      <v-toolbar color="primary">
        <v-toolbar-title>{{ routeTitle }}</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn icon>
          <v-icon>mdi-magnify</v-icon>
        </v-btn>
        <v-btn icon>
          <v-icon>mdi-dots-vertical</v-icon>
        </v-btn>
        <template #extension>
          <v-tabs v-model="filter">
            <v-tab
              v-for="item in tabItems"
              :key="item.value"
              :value="item.value"
            >
              {{ item.label }}
            </v-tab>
          </v-tabs>
        </template>
      </v-toolbar>
      <v-card-text>
        <v-window v-model="filter">
          <v-window-item v-for="type in vacancyTypes" :key="type" :value="type">
            <gm-vacancies-table :type="type" />
          </v-window-item>
        </v-window>
      </v-card-text>
    </v-card>
  </gm-route-page>
</template>
