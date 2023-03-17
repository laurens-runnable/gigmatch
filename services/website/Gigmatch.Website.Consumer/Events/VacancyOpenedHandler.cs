using Gigmatch.Website.Consumer.Model;
using Gigmatch.Website.Model;
using Microsoft.Extensions.Logging;
using SolTechnology.Avro;

namespace Gigmatch.Website.Consumer.Events;

internal class VacancyOpenedHandler : IEventHandler
{
    private readonly ILogger<VacancyOpenedHandler> _logger;

    private readonly IVacancyRepository _vacancyRepository;

    private readonly ISkillRepository _skillRepository;

    public VacancyOpenedHandler(ILogger<VacancyOpenedHandler> logger, IVacancyRepository vacancyRepository,
        ISkillRepository skillRepository)
    {
        _logger = logger;
        _vacancyRepository = vacancyRepository;
        _skillRepository = skillRepository;
    }

    public async Task HandleEventAsync(byte[] data)
    {
        var ev = AvroConvert.DeserializeHeadless<VacancyOpened>(data);

        var vacancy = await ev.ToVacancy(_skillRepository);
        _logger.LogDebug("Saving vacancy {id}", vacancy.Id);
        await _vacancyRepository.SaveVacancyAsync(vacancy);
    }
}

file static class VacancyOpenedExtensions
{
    internal static async Task<Vacancy> ToVacancy(this VacancyOpened ev, ISkillRepository skillRepository)
    {
        var experienceItems = new List<Vacancy.ExperienceItem>();
        foreach (var experience in ev.experience)
        {
            var skill = await skillRepository.FindSkillAsync(experience.skillId);
            if (skill != null)
            {
                experienceItems.Add(new Vacancy.ExperienceItem
                {
                    Skill = skill,
                    Level = (int)experience.level
                });
            }
        }

        return new Vacancy
        {
            Id = ev.id,
            JobTitle = ev.jobTitle,
            Experience = experienceItems,
            Start = ev.start,
            End = ev.end,
            Deadline = ev.deadline
        };
    }
}