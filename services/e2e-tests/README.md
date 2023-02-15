# End-to-end tests

[Playwright](https://playwright.dev) end-to-end tests

## Requirements

- All components running on `localhost`.
- The [Cloud Gateway](../../cloud) should be available at `http://localhost:8080`
- The [consumer-dashboard](../dashboard/consumer/README.md) should have test synchronization enabled
- Spring Boot active profiles:
    - `local`
    - `test-set`

## Install and run

```bash
# Install dependencies
npm install

# Run tests, use command-line reporter
npx playwright test

# Use HTML reporter
npx playwright test --reporter=html
npx playwright show-report

# Run tests using specific browser
npx playwright test --project chromium
npx playwright test --project firefox
npx playwright test --project webkit

# Or use package.json scripts instead
npm test
npm start test-chromium
npm start test-firefox
npm start test-webkit
```

## Test synchronization

Testing an asynchronous event-driven architecture requires explicit synchronization during test setup. I.e. tests must
be able to await the asynchronous completion of their setup. The architecture uses dedicated synchronization events to
achieve this.

1. Tests signal the start and completion of their setup by sending commands to `match-service`. These commands result in
   `TestSetupStarted` and `TestSetupCompleted` events respectively.
2. In response to these events, `dashboard-consumer` updates a dedicated synchronization table. (Which happens to be a
   MongoDB collection.)
3. The test then polls the synchronization table for setup completion, throwing an exception in case of a timeout.
