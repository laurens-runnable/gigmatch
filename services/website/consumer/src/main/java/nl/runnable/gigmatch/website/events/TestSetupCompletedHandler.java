package nl.runnable.gigmatch.website.events;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.runnable.gigmatch.events.TestSetupCompleted;
import nl.runnable.gigmatch.model.TestSetup;
import nl.runnable.gigmatch.model.TestSetupRepository;

import java.util.concurrent.CompletionStage;

import static nl.runnable.gigmatch.website.events.EventConstants.HANDLER_SUFFIX;

@ApplicationScoped
@Named("nl.runnable.gigmatch.events.TestSetupCompleted" + HANDLER_SUFFIX)
@Slf4j
public class TestSetupCompletedHandler implements EventHandler<TestSetupCompleted> {

    private final TestSetupRepository testSetupRepository;

    public TestSetupCompletedHandler(@NonNull TestSetupRepository testSetupRepository) {
        this.testSetupRepository = testSetupRepository;
    }

    @Override
    public CompletionStage<Void> handleEvent(TestSetupCompleted event) {
        return testSetupRepository.saveTestSetup(new TestSetup(event.getId(), false));
    }
}
