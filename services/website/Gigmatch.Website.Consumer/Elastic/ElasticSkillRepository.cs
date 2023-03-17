using Gigmatch.Website.Consumer.Model;
using Gigmatch.Website.Model;
using Nest;

namespace Gigmatch.Website.Consumer.Elastic;

internal class ElasticSkillRepository : ElasticRepository, ISkillRepository
{
    public ElasticSkillRepository(ElasticClient client) :
        base(client, ElasticConstants.IndexNames.Skill)
    {
    }

    public async Task<Skill?> FindSkillAsync(Guid id)
    {
        var response = await Client.SearchAsync<Skill>(search => search
            .Index(IndexName)
            .Query(query => query
                .Bool(boolean => boolean
                    .Must(must => must
                        .Term(x => x.Id, id)
                    )
                )
            )
        );

        return response.Documents.SingleOrDefault();
    }

    public Task SaveSkillAsync(Skill skill) =>
        Client.IndexAsync(skill, idx => idx.Index(IndexName));

    public Task DeleteSkillAsync(Guid id) =>
        Client.DeleteAsync<Skill>(id, idx => idx.Index(IndexName));
}