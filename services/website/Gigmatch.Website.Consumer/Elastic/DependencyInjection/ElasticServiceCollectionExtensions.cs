using Gigmatch.Website.Consumer.Model;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Options;
using Nest;

namespace Gigmatch.Website.Consumer.Elastic.DependencyInjection;

internal static class ElasticServiceCollectionExtensions
{
    internal static IServiceCollection AddElasticRepositories(this IServiceCollection services)
    {
        services.AddSingleton<ElasticClient>(s =>
        {
            var options = s.GetRequiredService<IOptions<ElasticOptions>>().Value;
            return new ElasticClient(options.Uri);
        });
        services.AddSingleton<ISkillRepository, ElasticSkillRepository>();
        services.AddSingleton<IVacancyRepository, ElasticVacancyRepository>();
        services.AddSingleton<ITestSetupSynchronization, ElasticTestSetupSynchronization>();
        return services;
    }
}