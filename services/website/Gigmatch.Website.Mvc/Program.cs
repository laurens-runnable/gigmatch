using Gigmatch.Website.Mvc.Elastic;
using Gigmatch.Website.Mvc.Elastic.DependencyInjection;
using Gigmatch.Website.Mvc.Middleware;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authentication.OpenIdConnect;

var builder = WebApplication.CreateBuilder(args);

var services = builder.Services;
var gigmatchConfig = builder.Configuration.GetSection("Gigmatch");

services.Configure<ElasticOptions>(gigmatchConfig.GetSection("Elastic"));
services.AddElasticRepositories();

services.AddAuthentication(options =>
    {
        options.DefaultScheme = CookieAuthenticationDefaults.AuthenticationScheme;
        options.DefaultChallengeScheme = OpenIdConnectDefaults.AuthenticationScheme;
    })
    .AddCookie(options =>
    {
        options.Cookie.Name = "gm.website";
        options.Cookie.SameSite = SameSiteMode.Strict;
        options.Cookie.SecurePolicy = CookieSecurePolicy.SameAsRequest;
    })
    .AddOpenIdConnect(options =>
    {
        var openId = gigmatchConfig.GetSection("OpenId");
        options.Authority = openId.GetValue<string>("Authority");
        options.ClientId = openId.GetValue<string>("ClientId");
        options.ClientSecret = openId.GetValue<string>("ClientSecret");
        options.ResponseType = "code";
        options.RequireHttpsMetadata = !builder.Environment.IsDevelopment();
        options.GetClaimsFromUserInfoEndpoint = true;
        options.SaveTokens = true;
        options.NonceCookie.SameSite = SameSiteMode.Strict;
        options.NonceCookie.SecurePolicy = CookieSecurePolicy.SameAsRequest;
        options.CorrelationCookie.SameSite = SameSiteMode.Strict;
        options.CorrelationCookie.SecurePolicy = CookieSecurePolicy.SameAsRequest;
    });
services.AddAuthorization();
services.AddMvc();

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseDeveloperExceptionPage();

    var hostRewriting = gigmatchConfig.GetSection("HostRewriting");
    if (hostRewriting.Exists())
    {
        var sourceHost = new HostString(hostRewriting.GetValue<string>("SourceHost")!);
        var targetHost = new HostString(hostRewriting.GetValue<string>("TargetHost")!);
        app.UseHostRewriting(sourceHost, targetHost);
    }
}

if (!app.Environment.IsDevelopment())
{
    app.UseHsts();
}

app.UsePathBase(gigmatchConfig.GetValue<string>("Routing:PathBase"));
app.UseStaticFiles();
app.UseRouting();

app.UseAuthentication();
app.UseAuthorization();

app.MapControllers().RequireAuthorization();

app.Run();