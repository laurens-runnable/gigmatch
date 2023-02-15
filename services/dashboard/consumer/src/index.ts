import { createApplication } from './application'
import { createConfig } from './config'

void (async function () {
  const config = createConfig()
  const application = createApplication(config)

  await application.startup()
  process.on('SIGINT', () => {
    application
      .shutdown()
      .then(() => {})
      .catch(() => {})
  })
})()
