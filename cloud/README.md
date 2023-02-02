# Spring Cloud services

Infrastructure for the main application.

* [gateway](gateway) (Spring Cloud Gateway)
* [config-server](config-server) (Spring Cloud Config)
* [discovery-server](discovery-server) (Spring Cloud Eureka)

## Gateway URL mapping

Services are accessed through the Gateway.

| Path         | Service                                    |
|--------------|--------------------------------------------|
| `/dashboard` | [dashboard](../services/dashboard/app)     |
| `/match`     | [match-service](../services/match-service) |
| `/website`   | [website](../services/website)             |
| `/auth`      | Keycloak                                   |

> To reduce service registration overhead during local development, the services are mapped directly to their
> `localhost` endpoints.
>
> [local configuration](./gateway/src/main/resources/application-local.properties)

## Build

```bash
# Build Docker images
./bin/build-config-server.sh
./bin/build-discovery-server.sh
./bin/build-gateway.sh
```
