namespace Gigmatch.Website.Consumer.Model;

internal interface ISkillRepository
{
    Task SaveSkillAsync(Skill skill);

    Task DeleteSkillAsync(Guid id);
}