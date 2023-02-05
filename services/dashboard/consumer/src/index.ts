import { createApplication } from './application'
import { createConfig } from './config'

;(async function () {
  const config = createConfig()
  const application = createApplication(config)

  await application.startup()
  process.on('SIGTERM', async () => {
    await application.shutdown()
  })
})()
