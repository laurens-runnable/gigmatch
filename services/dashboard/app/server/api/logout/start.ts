export default defineEventHandler(async (event) => {
  await startLogout(event)
})
