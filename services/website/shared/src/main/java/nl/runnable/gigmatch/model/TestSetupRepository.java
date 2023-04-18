package nl.runnable.gigmatch.model;

import lombok.NonNull;

import java.util.concurrent.CompletionStage;

public interface TestSetupRepository {

    CompletionStage<Void> saveTestSetup(@NonNull TestSetup testSetup);
}
