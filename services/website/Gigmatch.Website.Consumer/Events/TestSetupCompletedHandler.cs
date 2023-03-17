using Gigmatch.Website.Consumer.Model;
using Microsoft.Extensions.Logging;
using SolTechnology.Avro;

namespace Gigmatch.Website.Consumer.Events;

public class TestSetupCompletedHandler : IEventHandler
{
    private readonly ILogger<TestSetupCompletedHandler> _logger;

    private readonly ITestSetupSynchronization _testSetupSynchronization;

    public TestSetupCompletedHandler(ILogger<TestSetupCompletedHandler> logger,
        ITestSetupSynchronization testSetupSynchronization)
    {
        _logger = logger;
        _testSetupSynchronization = testSetupSynchronization;
    }

    public Task HandleEventAsync(byte[] data)
    {
        var ev = AvroConvert.DeserializeHeadless<TestSetupCompleted>(data);
        _logger.LogInformation("Completing test setup {id}", ev.id);
        return _testSetupSynchronization.completeTestSetupAsync(ev.id);
    }
}