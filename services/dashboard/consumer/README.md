# dashboard-consumer

```bash
# Install dependencies
npm install

# Run in development mode
npm run dev

# Format code (using Prettier)
npm run format

# Lint
npm run lint

# Build Docker image
./bin/build-image.sh
```

## Configuration

See [config.ts](./src/config.ts) for the environment variables and their defaults.

For integration with the [end-to-end tests](../../e2e-tests) `GIGMATCH_E2E` must be set to `true`.

With this setting enabled, the Consumer maintains a Mongo test IDs.

## Test synchronization

Enable test synchronization using the environment variable:

```
GIGMATCH_TEST_SYNCHRONIZATION=true
```

For more details [see here](../../e2e-tests/README.md)

## Actuator endpoints

The application provides Spring Boot-like endpoints for monitoring application health:

- [/health](http://localhost:3001/health)
- [/info](http://localhost:3001/info)
- [/metrics](http://localhost:3001/metrics)
