using Gigmatch.Website.Consumer.Model;
using Microsoft.Extensions.Logging;
using SolTechnology.Avro;

namespace Gigmatch.Website.Consumer.Events;

public class TestSetupStartedHandler : IEventHandler
{
    private readonly ILogger<TestSetupStartedHandler> _logger;

    private readonly ITestSetupSynchronization _testSetupSynchronization;

    public TestSetupStartedHandler(ILogger<TestSetupStartedHandler> logger,
        ITestSetupSynchronization testSetupSynchronization)
    {
        _logger = logger;
        _testSetupSynchronization = testSetupSynchronization;
    }

    public Task HandleEventAsync(byte[] data)
    {
        var ev = AvroConvert.DeserializeHeadless<TestSetupStarted>(data);
        _logger.LogInformation("Starting test setup {id}", ev.id);
        return _testSetupSynchronization.startTestSetupAsync(ev.id);
    }
}