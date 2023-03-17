using Gigmatch.Website.Consumer.Model;
using Microsoft.Extensions.Logging;

namespace Gigmatch.Website.Consumer.Events;

internal class VacanciesResetHandler : IEventHandler
{
    private readonly ILogger<VacanciesResetHandler> _logger;

    private readonly IVacancyRepository _vacancyRepository;

    public VacanciesResetHandler(ILogger<VacanciesResetHandler> logger, IVacancyRepository vacancyRepository)
    {
        _logger = logger;
        _vacancyRepository = vacancyRepository;
    }

    public async Task HandleEventAsync(byte[] data)
    {
        _logger.LogInformation("Deleting all vacancies");
        await _vacancyRepository.DeleteAllVacanciesAsync();
    }
}