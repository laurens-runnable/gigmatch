using Gigmatch.Website.Consumer.Model;
using Nest;

namespace Gigmatch.Website.Consumer.Elastic;

internal class ElasticSkillRepository : ElasticRepository, ISkillRepository
{
    public ElasticSkillRepository(ElasticClient client) :
        base(client, ElasticConstants.IndexNames.Skill)
    {
    }

    public Task SaveSkillAsync(Skill skill) =>
        Client.IndexAsync(skill, idx => idx.Index(IndexName));

    public Task DeleteSkillAsync(Guid id) =>
        Client.DeleteAsync<Skill>(id, idx => idx.Index(IndexName));
}