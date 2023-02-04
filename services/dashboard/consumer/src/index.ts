import { Application } from './application'

;(async function () {
  const application = new Application()
  await application.startup()
  process.on('SIGTERM', async () => {
    await application.shutdown()
  })
})()
