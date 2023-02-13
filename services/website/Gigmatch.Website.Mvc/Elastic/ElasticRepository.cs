using Nest;

namespace Gigmatch.Website.Mvc.Elastic;

internal abstract class ElasticRepository
{
    protected ElasticRepository(ElasticClient client)
    {
        Client = client;
    }

    protected ElasticClient Client { get; }
}