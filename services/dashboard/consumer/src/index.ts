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

const shutdownApplication = (): void => {
  application
    .shutdown()
    .then(() => {})
    .catch(handleLifecycleError)
}

if (process.env.TS_NODE_DEV === 'true') {
  // Listen for SIGTERM when running under ts-node-dev
  process.on('SIGTERM', shutdownApplication)
} else {
  // Otherwise listen for SIGINT
  process.on('SIGINT', shutdownApplication)
}
