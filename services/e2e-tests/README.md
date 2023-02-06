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

An inherent difficulty in testing event-driven architecture is that it is asynchronous. There is no easy way of knowing
when a given test setup has completed.

To synchronize setup, tests use the following synchronization mechanism:

1. The test sends a command to the `match-service` that results in a `TestSetupStarted` event.
2. In response to `TestSetupStarted`, the `dashboard-consumer` inserts an entry in a synchronization table.
3. After setup has completed, the test sends a command that results in a `TestSetupCompleted` event.
4. In response to `TestSetupCompleted`, the `dashboard-consumer` updates the synchronization entry.
5. Finally, the test queries the synchronization entries for test setup completion, subject to a timeout.

Relevant code:

* [test-setup fixture](./tests/fixtures/test-setup.ts)
* [test-setup event handlers](../dashboard/consumer/src/application/handlers/test-setup.ts)
