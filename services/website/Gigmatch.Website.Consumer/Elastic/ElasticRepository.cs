using Nest;

namespace Gigmatch.Website.Consumer.Elastic;

internal abstract class ElasticRepository
{
    protected ElasticRepository(ElasticClient client, string indexName)
    {
        Client = client;
        IndexName = indexName;
    }

    protected ElasticClient Client { get; }

    protected string IndexName { get; }
}