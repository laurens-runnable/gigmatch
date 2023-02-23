import { createApplication } from './application'
import { createConfig } from './config'

const config = createConfig()
const application = createApplication(config)

function monitorApplication(): void {
  setTimeout(() => {
    if (!application.isRunning) {
      process.exit(0)
    } else {
      monitorApplication()
    }
  }, 1000)
}

function handleLifecycleError(err: Error): void {
  console.error(err)
  process.exit(-1)
}

application.startup().then(monitorApplication).catch(handleLifecycleError)

process.on('SIGINT', () => {
  application
    .shutdown()
    .then(() => {})
    .catch(handleLifecycleError)
})
