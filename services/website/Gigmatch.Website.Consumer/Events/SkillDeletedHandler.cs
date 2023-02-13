using Gigmatch.Website.Consumer.Model;
using Microsoft.Extensions.Logging;
using SolTechnology.Avro;

namespace Gigmatch.Website.Consumer.Events;

internal class SkillDeletedHandler : IEventHandler
{
    private readonly ILogger<SkillCreatedOrUpdatedHandler> _logger;

    private readonly ISkillRepository _skillRepository;

    public SkillDeletedHandler(ILogger<SkillCreatedOrUpdatedHandler> logger, ISkillRepository skillRepository)
    {
        _logger = logger;
        _skillRepository = skillRepository;
    }

    public async Task HandleEventAsync(byte[] data)
    {
        var ev = AvroConvert.DeserializeHeadless<SkillDeleted>(data);
        _logger.LogInformation("Deleting skill {id}", ev.id);
        await _skillRepository.DeleteSkillAsync(ev.id);
    }
}