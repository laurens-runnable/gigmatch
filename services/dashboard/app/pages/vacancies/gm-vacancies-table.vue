<script setup lang="ts">
import { useQuery } from '@vue/apollo-composable'
import gql from 'graphql-tag'

const props = defineProps<{ type: string }>()

const { result } = useQuery(
  gql`
    query Vacancies($type: VacancyType) {
      vacancies(filter: { type: $type }) {
        id
        jobTitle
        start
        end
        deadline
      }
    }
  `,
  { type: props.type?.toUpperCase() }
)

const { t } = useI18n()

const headers = [
  {
    title: t('vacancies.jobTitle'),
    key: 'jobTitle',
  },
  {
    title: t('vacancies.deadline'),
    key: 'deadline',
  },
]

const vacancies = computed(() => result.value?.vacancies ?? [])
</script>

<template>
  <v-data-table :headers="headers" :items="vacancies"></v-data-table>
</template>
