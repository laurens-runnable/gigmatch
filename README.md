# Gigmatch

Personal reference implementation of an event-driven microservice architecture.

The example application is a job matching platform.

## Overview

![Overview](./diagrams/overview.png)

Additional infrastructure, not shown in the diagram:

* Keycloak  
  Authentication and identity management
* Spring Cloud  
  Gateway, configuration and service discovery

## Local development

### Requirements

* OpenJDK 17
* Node.js 18
* Docker

### Docker containers

To get started with local development, you can run Docker containers for
the [Infrastructure](./local-dev/infrastructure.yml) and, optionally, the [Cloud services](./local-dev/cloud.yml).

```bash
# Start/Stop only Infrastructure containers
./local-dev/bin/up.sh
./local-dev/bin/down.sh
./local-dev/bin/follow-logs.sh

# Start/Stop both Infrastructure and Cloud containers
./local-dev/bin/cloud/up.sh
./local-dev/bin/cloud/down.sh
./local-dev/bin/cloud/follow-logs.sh

# Clean up Docker resources afterwards
docker system prune
docker volume prune
```

## Codebase

* [Application services](./services)  
  The main application services
* [Cloud services](./cloud)  
  Spring Cloud services

## License

[Public Domain](LICENSE)
