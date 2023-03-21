# dashboard-app

## Install and run

```bash
# Install dependencies
npm install

# Run development server
npm run dev

# Format code (using Prettier)
npm run format

# Lint
npm run lint
```

## Session storage

The application uses cookies to store session state, including JWT access and refresh tokens. This "session" cookie
is `http-only` and `secure` (for HTTPS connections).

In production scenarios it might be preferable to store session state on the server, in, for example
a [Redis cache](https://github.com/sidebase/nuxt-session).

> In general, using JWTs in sessions
> is [not recommended](http://cryto.net/~joepie91/blog/2016/06/13/stop-using-jwt-for-sessions/).
> But since this is a demo application, I chose to keep authorization relatively simple.
