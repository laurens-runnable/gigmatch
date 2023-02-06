# End-to-end tests

[Playwright](https://playwright.dev) end-to-end tests

## Requirements

* All components running on `localhost`.
* The [Cloud Gateway](../../cloud) should be available at `http://localhost:8080`
* Spring Boot active profiles:
    * `local`
    * `test-set`

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
