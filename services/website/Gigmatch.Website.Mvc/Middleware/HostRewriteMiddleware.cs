namespace Gigmatch.Website.Mvc.Middleware;

/// <summary>
/// Middleware that rewrites the Host header.
/// Its use case is to rewrite requests from Docker containers to services running in local development environments. 
/// </summary>
internal class HostRewriteMiddleware
{
    private readonly RequestDelegate _next;

    private readonly HostString _source;

    private readonly HostString _target;

    public HostRewriteMiddleware(RequestDelegate next, ILoggerFactory loggerFactory,
        HostString source, HostString target)
    {
        _next = next;
        _source = source;
        _target = target;
        var logger = loggerFactory.CreateLogger<HostRewriteMiddleware>();
        logger.LogInformation("Rewriting Host request header '{}' to '{}'", _source, _target);
    }

    public async Task InvokeAsync(HttpContext context)
    {
        if (context.Request.Host == _source)
        {
            context.Request.Host = _target;
        }

        await _next.Invoke(context);
    }
}