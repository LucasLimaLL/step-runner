package br.com.lucaslima.steprunner.application.domains;

import java.util.Optional;

public record Request(Optional<Object> workflowInput,
                      Optional<String> lastExecutedStepName,
                      Optional<Object> executionId) {

    public static Request firstRun(Object workflowInput) {
        return new Request(Optional.ofNullable(workflowInput), Optional.empty(), Optional.empty());
    }

    public static Request retryFrom(String lastExecutedStepName, Object workflowInput) {
        return new Request(Optional.ofNullable(workflowInput), Optional.ofNullable(lastExecutedStepName), Optional.empty());
    }
}
