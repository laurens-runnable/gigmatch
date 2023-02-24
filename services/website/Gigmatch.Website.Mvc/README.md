# Website

```bash
# Run app
dotnet run http
# In watch mode
dotnet watch run http
```

## Session storage

The application uses cookies to store session state, including JWT access and refresh tokens. This "session" cookie
is `http-only` and `secure` (for HTTPS connections), so security risks are minimized.

In production scenarios it might be preferable to store session state on the server, in, for example
a [Redis cache](https://learn.microsoft.com/en-us/azure/azure-cache-for-redis/cache-aspnet-session-state-provider).
