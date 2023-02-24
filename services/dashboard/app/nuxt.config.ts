// https://nuxt.com/docs/api/configuration/nuxt-config

export default defineNuxtConfig({
  ssr: false,
  alias: {
    'dashboard-shared': './node_modules/@gigmatch/dashboard-shared',
  },
  typescript: { strict: true },
  runtimeConfig: {
    session: {
      cookieName: 'gm.cookie',
      password: 'YcFjVdBAc9uEdXdjyQZQEJNWuJ0QG8bY',
      secure: process.env.NODE_ENV === 'production',
    },
    openId: {
      discoveryUrl: 'http://localhost:8080/auth/realms/gigmatch',
      clientId: 'gigmatch-dashboard',
      clientSecret: 'secret',
      redirectUri: 'http://localhost:8080/dashboard/api/authorize/end',
      postLogoutRedirectUri: 'http://localhost:8080/dashboard/api/logout/end',
    },
    mongo: {
      url: 'mongodb://root:root@localhost',
      database: 'gigmatch',
    },
    matchService: {
      baseUrl: 'http://localhost:8080/matches',
      timeout: 6000,
      retries: 3,
    },
    keycloak: {
      url: 'http://localhost:8080/auth',
      username: 'admin',
      password: 'admin',
      realm: 'gigmatch',
      clientId: 'admin-cli',
    },
  },
  css: ['vuetify/lib/styles/main.sass'],
  build: { transpile: ['vuetify'] },
  vite: { define: { 'process.env.DEBUG': false } },
})
