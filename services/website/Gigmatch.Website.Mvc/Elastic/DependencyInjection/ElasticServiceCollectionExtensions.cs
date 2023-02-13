using Gigmatch.Website.Mvc.Models;
using Microsoft.Extensions.Options;
using Nest;

namespace Gigmatch.Website.Mvc.Elastic.DependencyInjection;

internal static class ElasticServiceCollectionExtensions
{
    internal static IServiceCollection AddElasticRepositories(this IServiceCollection services)
    {
        services.AddSingleton<ElasticClient>(s =>
        {
            var options = s.GetRequiredService<IOptions<ElasticOptions>>().Value;
            return new ElasticClient(options.Uri);
        });
        services.AddSingleton<IVacancyRepository, ElasticVacancyRepository>();
        return services;
    }
}