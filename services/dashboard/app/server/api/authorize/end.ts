export default defineEventHandler(async (event) => {
  await endAuthorizationFlow(event)
})
