# Local development

## Requirements

* OpenJDK 17
* Node.js 18
* Docker

## Docker containers

To get started with local development, you can run Docker containers for the [Infrastructure](./infrastructure.yml) and,
optionally, [Cloud services](./cloud.yml).

```bash
# Start/Stop only Infrastructure containers
./bin/up.sh
./bin/down.sh
./bin/follow-logs.sh

# Start/Stop both Infrastructure and Cloud containers
./bin/cloud/up.sh
./bin/cloud/down.sh
./bin/cloud/follow-logs.sh

# Clean up Docker resources afterwards
docker system prune
docker volume prune
```
