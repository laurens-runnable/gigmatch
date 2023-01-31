# End-to-end tests

[Playwright](https://playwright.dev) end-to-end tests

## Requirements

* All components running on `localhost`.
* Spring Boot active profiles:
  * `local`
  * `test-set` 
* The [Gateway](../../cloud/gateway) should be available at the test base URL `http://localhost:8080`.

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

## Or use scripts instead
npm test
npm start test-chromium
npm start test-firefox
npm start test-webkit
```
