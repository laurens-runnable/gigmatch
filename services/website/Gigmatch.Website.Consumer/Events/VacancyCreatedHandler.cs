using Gigmatch.Website.Consumer.Model;
using Microsoft.Extensions.Logging;
using SolTechnology.Avro;

namespace Gigmatch.Website.Consumer.Events;

internal class VacancyCreatedHandler : IEventHandler
{
    private readonly ILogger<VacancyCreatedHandler> _logger;

    private readonly IVacancyRepository _vacancyRepository;

    public VacancyCreatedHandler(ILogger<VacancyCreatedHandler> logger, IVacancyRepository _vacancyRepository)
    {
        _logger = logger;
        this._vacancyRepository = _vacancyRepository;
    }

    public Task HandleEventAsync(byte[] data)
    {
        var ev = AvroConvert.DeserializeHeadless<VacancyCreated>(data);

        var vacancy = ev.ToVacancy();
        _logger.LogDebug("Saving vacancy {id}", vacancy.Id);
        return _vacancyRepository.SaveVacancyAsync(vacancy);
    }
}

file static class VacancyCreatedExtensions
{
    internal static Vacancy ToVacancy(this VacancyCreated ev) =>
        new()
        {
            Id = ev.id,
            Name = ev.name,
            Start = ev.start,
        };
}