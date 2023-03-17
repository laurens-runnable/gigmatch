# dashboard-consumer

```bash
# Install dependencies
npm install

# Run in development mode
npm run dev

# Build
npm run build

# Format code (using Prettier)
npm run format

# Lint
npm run lint

# Build Docker image
./bin/build-image.sh
```

## Actuator endpoints

The application provides Spring Boot-like endpoints for monitoring application health:

- [/health](http://localhost:3001/health)
- [/info](http://localhost:3001/info)
- [/metrics](http://localhost:3001/metrics)
