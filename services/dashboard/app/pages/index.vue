<script setup>
import { useSeoMeta } from '#head'

useSeoMeta({ title: 'Home' })
</script>

<template>
  <header>
    <nav>
      <ul>
        <li>
          <NuxtLink to="/"> Home </NuxtLink>
        </li>
        <li>
          <NuxtLink to="/vacancies"> Vacancies </NuxtLink>
        </li>
      </ul>
    </nav>
  </header>
</template>
