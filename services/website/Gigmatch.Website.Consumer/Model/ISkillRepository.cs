using Gigmatch.Website.Model;

namespace Gigmatch.Website.Consumer.Model;

internal interface ISkillRepository
{
    Task<Skill?> FindSkillAsync(Guid id);

    Task SaveSkillAsync(Skill skill);

    Task DeleteSkillAsync(Guid id);
}