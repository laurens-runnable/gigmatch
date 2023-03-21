export default defineEventHandler(async (event) => {
  await endLogout(event)
})
