<script setup>
import { useSeoMeta } from "#head";

const config = useRuntimeConfig();

const counter = reactive({
  count: 0
});

// counter.count += 1;

function increment() {
  counter.count += 1;
}


useSeoMeta({ title: "Home" });
</script>

<template>
  <header>
    <nav>
      <ul>
        <li>
          <NuxtLink to="/">Home</NuxtLink>
        </li>
        <li>
          <NuxtLink to="/vacancies">Vacancies</NuxtLink>
        </li>
      </ul>
    </nav>
  </header>
  <main>
    <p>
      Counter: {{ counter.count }}
    </p>
    <button @click="increment">+</button>
  </main>
</template>
