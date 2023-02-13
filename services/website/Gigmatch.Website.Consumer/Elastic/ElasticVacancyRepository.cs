using Gigmatch.Website.Consumer.Model;
using Nest;

namespace Gigmatch.Website.Consumer.Elastic;

internal class ElasticVacancyRepository : ElasticRepository, IVacancyRepository
{
    public ElasticVacancyRepository(ElasticClient client) :
        base(client, ElasticConstants.IndexNames.Vacancy)
    {
    }

    public Task SaveVacancyAsync(Vacancy vacancy) =>
        Client.IndexAsync(vacancy, idx => idx.Index(IndexName));

    public Task DeleteAllVacanciesAsync() =>
        Client.DeleteByQueryAsync<Vacancy>(descriptor => descriptor
            .Query(query => query.MatchAll())
            .Index(IndexName)
        );
}