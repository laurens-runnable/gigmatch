using Gigmatch.Website.Model;
using Gigmatch.Website.Mvc.Models;
using Nest;

namespace Gigmatch.Website.Mvc.Elastic;

internal class ElasticVacancyRepository : ElasticRepository, IVacancyRepository
{
    public ElasticVacancyRepository(ElasticClient client) :
        base(client)
    {
    }

    public async Task<IEnumerable<Vacancy>> FindAllAsync(int offset, int size)
    {
        var response = await Client.SearchAsync<Vacancy>(search => search
            .Index(ElasticConstants.IndexNames.Vacancy)
            .Query(query => query.MatchAll())
            .From(offset)
            .Size(size)
        );
        return response.Documents.AsEnumerable();
    }
}