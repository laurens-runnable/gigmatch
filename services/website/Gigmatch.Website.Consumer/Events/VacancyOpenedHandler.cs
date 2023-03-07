using Gigmatch.Website.Consumer.Model;
using Microsoft.Extensions.Logging;
using SolTechnology.Avro;

namespace Gigmatch.Website.Consumer.Events;

internal class VacancyOpenedHandler : IEventHandler
{
    private readonly ILogger<VacancyOpenedHandler> _logger;

    private readonly IVacancyRepository _vacancyRepository;

    public VacancyOpenedHandler(ILogger<VacancyOpenedHandler> logger, IVacancyRepository _vacancyRepository)
    {
        _logger = logger;
        this._vacancyRepository = _vacancyRepository;
    }

    public Task HandleEventAsync(byte[] data)
    {
        var ev = AvroConvert.DeserializeHeadless<VacancyOpened>(data);

        var vacancy = ev.ToVacancy();
        _logger.LogDebug("Saving vacancy {id}", vacancy.Id);
        return _vacancyRepository.SaveVacancyAsync(vacancy);
    }
}

file static class VacancyOpenedExtensions
{
    internal static Vacancy ToVacancy(this VacancyOpened ev) =>
        new()
        {
            Id = ev.id,
            JobTitle = ev.jobTitle,
            Skills = ev.skills.Select(x => new Guid(x)).ToArray(),
            Start = ev.start,
            End = ev.end,
        };
}
