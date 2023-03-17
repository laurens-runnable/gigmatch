using Gigmatch.Website.Consumer.Model;
using Gigmatch.Website.Model;
using Microsoft.Extensions.Logging;
using SolTechnology.Avro;

namespace Gigmatch.Website.Consumer.Events;

internal class SkillCreatedOrUpdatedHandler : IEventHandler
{
    private readonly ILogger<SkillCreatedOrUpdatedHandler> _logger;

    private readonly ISkillRepository _skillRepository;

    public SkillCreatedOrUpdatedHandler(ILogger<SkillCreatedOrUpdatedHandler> logger,
        ISkillRepository skillRepository)
    {
        _logger = logger;
        _skillRepository = skillRepository;
    }

    public async Task HandleEventAsync(byte[] data)
    {
        var ev = AvroConvert.DeserializeHeadless<SkillCreatedOrUpdated>(data);

        var skill = ev.ToSkill();
        _logger.LogDebug("Saving skill {id}", skill.Id);
        await _skillRepository.SaveSkillAsync(skill);
    }
}

file static class SkillCreatedOrUpdatedExtensions
{
    internal static Skill ToSkill(this SkillCreatedOrUpdated ev) =>
        new()
        {
            Id = ev.id,
            Name = ev.name,
            Slug = ev.slug,
        };
}