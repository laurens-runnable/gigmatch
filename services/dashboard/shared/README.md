# dashboard-shared

Shared code for [dashboard-app](../app) and [dashboard-consumer](../consumer).

## Link

```bash
# Create global link for @gigmatch/dashboard-shared
npm link

# Then link this package
cd ../consumer
npm link @gigmatch/dashboard-shared
```

## Usage

```typescript
import { createMongoConfig, openDatabase } from "./mongo";
import { createRetryConfig } from "./retry";
import { Collection } from "mongodb";

async function initializeMongo() {
  // Optional: Mongo config from environment variables
  const mongoConfig = createMongoConfig();

  // Optional: retry config
  const retryConfig = createRetryConfig();

  const database = await openDatabase(mongoConfig, retryConfig);

  // Initialize collections
  await database.collections.initialize();

  // Access collections
  const skill: Collection = database.collections.skill();
  const vacancy: Collection = database.collections.vacancy()
}
```

## Configuration

| Environment variable      | Default                     |
|---------------------------|-----------------------------|
| `GIGMATCH_MONGO_URL`      | `mongodb://localhost:27017` |
| `GIGMATCH_MONGO_USERNAME` | `root`                      |
| `GIGMATCH_MONGO_PASSWORD` | `root`                      |
| `GIGMATCH_MONGO_DATABASE` | `gigmatch`                  |
| `GIGMATCH_RETRY_RETRIES`  | `10`                        |
| `GIGMATCH_RETRY_TIMEOUT`  | `1000`                      |
| `GIGMATCH_RETRY_FACTOR`   | `2`                         |
