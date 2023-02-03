namespace Gigmatch.Website.Mvc.Middleware;

public static class ApplicationBuilderHostRewritingExtensions
{
    /// <summary>
    /// Rewrites the Host header to allow for reverse-proxying in local development environments.
    /// </summary>
    public static IApplicationBuilder UseHostRewriting(this IApplicationBuilder app,
        HostString sourceHost, HostString targetHost)
    {
        // var logger = app.ApplicationServices.GetService<ILogger>();
        // logger.LogInformation("Rewriting Host header from {source} to {target}", sourceHost, targetHost);
        app.UseMiddleware<HostRewriteMiddleware>(sourceHost, targetHost);
        return app;
    }
}