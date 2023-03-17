namespace Gigmatch.Website.Consumer.Model;

public interface ITestSetupSynchronization
{
    Task startTestSetupAsync(Guid testId);

    Task completeTestSetupAsync(Guid testId);
}