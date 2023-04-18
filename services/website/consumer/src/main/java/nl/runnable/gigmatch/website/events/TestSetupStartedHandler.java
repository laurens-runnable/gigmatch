package nl.runnable.gigmatch.website.events;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.runnable.gigmatch.events.TestSetupStarted;
import nl.runnable.gigmatch.model.TestSetup;
import nl.runnable.gigmatch.model.TestSetupRepository;

import java.util.concurrent.CompletionStage;

import static nl.runnable.gigmatch.website.events.EventConstants.HANDLER_SUFFIX;

@ApplicationScoped
@Named("nl.runnable.gigmatch.events.TestSetupStarted" + HANDLER_SUFFIX)
@Slf4j
public class TestSetupStartedHandler implements EventHandler<TestSetupStarted> {

    private final TestSetupRepository testSetupRepository;

    public TestSetupStartedHandler(@NonNull TestSetupRepository testSetupRepository) {
        this.testSetupRepository = testSetupRepository;
    }

    @Override
    public CompletionStage<Void> handleEvent(TestSetupStarted event) {
        return testSetupRepository.saveTestSetup(new TestSetup(event.getId(), true));
    }
}
