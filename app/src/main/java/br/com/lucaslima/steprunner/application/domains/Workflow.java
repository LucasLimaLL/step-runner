package br.com.lucaslima.steprunner.application.domains;

import br.com.lucaslima.steprunner.application.domains.retry.RetryOptions;
import br.com.lucaslima.steprunner.application.domains.routing.FailureHandling;
import br.com.lucaslima.steprunner.application.domains.steps.Step;

import java.util.List;

public record Workflow(
        String name,
        String version,
        List<Step> steps,
        RetryOptions retryOption,
        FailureHandling failureHandling
) {
    public Workflow {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Workflow name cannot be null or blank");
        }
        if (version == null || version.isBlank()) {
            throw new IllegalArgumentException("Workflow version cannot be null or blank");
        }
        if (steps == null || steps.isEmpty()) {
            throw new IllegalArgumentException("Workflow must have at least one step");
        }

    }
}
