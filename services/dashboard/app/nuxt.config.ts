// https://nuxt.com/docs/api/configuration/nuxt-config

export default defineNuxtConfig({
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
  },
})
