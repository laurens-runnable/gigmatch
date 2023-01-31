import { Eureka } from 'eureka-js-client'
import { EurekaConfig } from '../config'

export async function startClient(config: EurekaConfig) {
  return new Promise<Eureka>((resolve, reject) => {
    const { instance, eureka: { host, port, servicePath } } = config
    const client = new Eureka({
      instance: {
        app: instance.app,
        hostName: 'localhost',
        ipAddr: '127.0.0.1',
        statusPageUrl: `http://localhost:${instance.port}/health`,
        port: {
          '$': instance.port,
          '@enabled': true,
        },
        vipAddress: 'localhost',
        dataCenterInfo: {
          '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
          name: 'MyOwn',
        },
      },
      eureka: {
        host,
        port,
        servicePath,
        maxRetries: 10,
        preferIpAddress: true,
        fetchRegistry: false,
      },
    })
    client.start((err) => {
      if (err) {
        reject(err)
      } else {
        resolve(client)
      }
    })
  })
}

export async function register(config: EurekaConfig) {
  console.log('Registering with Eureka')
  const client = await startClient(config)
  return { unregister: () => stopClient(client) }
}

async function stopClient(client: Eureka) {
  return new Promise<void>((resolve, reject) => {
    client.stop((err) => {
      if (err) {
        reject(err)
      } else {
        resolve()
      }
    })
  })
}
