# dashboard-consumer

## Install and run

```bash
# Install dependencies
npm install

# Run in development mode
npm run dev

# Build Docker image
./bin/build-image.sh
```

## Configuration

| Environment variable                 | Default                     |
|--------------------------------------|-----------------------------|
| `GIGMATCH_AMQP_URL`                  | `ampq:://localhost`         |
| `GIGMATCH_AMQP_MATCH_EVENT_EXCHANGE` | `match-events`              |
| `GIGMATCH_AMQP_MATCH_EVENT_QUEUE`    | `match-events.admin`        |
| `GIGMATCH_MONGO_URL`                 | `mongodb://localhost:27017` |
| `GIGMATCH_MONGO_USERNAME`            | `root`                      |
| `GIGMATCH_MONGO_PASSWORD`            | `root`                      |
| `GIGMATCH_MONGO_DATABASE`            | `gigmatch`                  |
| `GIGMATCH_EUREKA_ENABLED`            | `false`                     |
| `GIGMATCH_EUREKA_HOST`               | `localhost`                 |
| `GIGMATCH_EUREKA_PORT`               | `8070`                      |
| `GIGMATCH_EUREKA_SERVICE_PATH`       | `/eureka/apps/`             |
| `GIGMATCH_ACTUATOR_PORT`             | `3001`                      |
| `GIGMATCH_RETRY_RETRIES`             | `10`                        |
| `GIGMATCH_RETRY_TIMEOUT`             | `1000`                      |
| `GIGMATCH_RETRY_FACTOR`              | `2`                         |

## Actuator endpoints

The application provides Spring Boot-like endpoints for monitoring application health:

* [/health](http://localhost:3001/health)
* [/info](http://localhost:3001/info)
* [/metrics](http://localhost:3001/metrics)
