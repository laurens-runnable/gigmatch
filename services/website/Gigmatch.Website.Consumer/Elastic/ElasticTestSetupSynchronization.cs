using Gigmatch.Website.Consumer.Model;
using Nest;

namespace Gigmatch.Website.Consumer.Elastic;

internal class ElasticTestSetupSynchronization : ElasticRepository, ITestSetupSynchronization
{
    private class TestSetup
    {
        public Guid Id { get; init; }

        public bool IsActive { get; init; }
    }

    public ElasticTestSetupSynchronization(ElasticClient client) : base(client, ElasticConstants.IndexNames.TestSetup)
    {
    }

    public Task startTestSetupAsync(Guid testId) =>
        Client.IndexAsync(
            new TestSetup { Id = testId, IsActive = true },
            selector => selector
                .Index(IndexName)
        );

    public Task completeTestSetupAsync(Guid testId) =>
        Client.UpdateAsync<TestSetup>(testId,
            update => update
                .Index(IndexName)
                .Doc(new TestSetup { Id = testId, IsActive = false })
        );
}