using Microsoft.Extensions.DependencyInjection;

namespace Gigmatch.Website.Consumer.Events.DependencyInjection;

internal static class EventServiceCollectionExtensions
{
    internal static IServiceCollection AddEventHandlers(this IServiceCollection services)
    {
        services.AddSingleton<SkillCreatedOrUpdatedHandler>();
        services.AddSingleton<SkillDeletedHandler>();
        services.AddSingleton<VacancyOpenedHandler>();
        services.AddSingleton<VacanciesResetHandler>();

        services.AddSingleton<EventHandlerResolver>(s => avroType =>
        {
            var modelType = avroType.Substring(avroType.LastIndexOf('.') + 1);
            return modelType switch
            {
                nameof(SkillCreatedOrUpdated) => s.GetRequiredService<SkillCreatedOrUpdatedHandler>(),
                nameof(SkillDeleted) => s.GetRequiredService<SkillDeletedHandler>(),
                nameof(VacancyOpened) => s.GetRequiredService<VacancyOpenedHandler>(),
                nameof(VacanciesReset) => s.GetRequiredService<VacanciesResetHandler>(),
                _ => null
            };
        });

        return services;
    }
}
