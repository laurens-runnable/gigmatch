# dashboard-consumer

```bash
# Install dependencies
npm install

# Run in development mode
npm run dev

# Build
npm run build

# Run, with test synchronization enabled (see below)
npm run start-e2e

# Format code (using Prettier)
npm run format

# Lint
npm run lint

# Build Docker image
./bin/build-image.sh
```

## Configuration

See [config.ts](./src/config.ts) for the environment variables and their defaults.

[Test synchronization](../../e2e-tests/README.md#test-synchronization) should be enabled when running end-to-end tests:

```
GIGMATCH_TEST_SYNCHRONIZATION=true
```

## Actuator endpoints

The application provides Spring Boot-like endpoints for monitoring application health:

- [/health](http://localhost:3001/health)
- [/info](http://localhost:3001/info)
- [/metrics](http://localhost:3001/metrics)
