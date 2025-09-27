package br.com.lucaslima.steprunner.application.domains;

import br.com.lucaslima.steprunner.application.ports.out.PlaceholderResolverPort;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class ResolutionContext {

    private final String workflowName;
    private final Map<String, Object> stepResults = new LinkedHashMap<>();
    private final PlaceholderResolverPort placeholderResolverPort;

    public ResolutionContext(String workflowName, PlaceholderResolverPort placeholderResolverPort) {

        if (workflowName == null || workflowName.isBlank()) {
            throw new IllegalArgumentException("workflowName cannot be null or blank");
        }
        this.workflowName = workflowName;
        this.placeholderResolverPort = Objects.requireNonNull(placeholderResolverPort);
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public Map<String, Object> getStepResults() {
        return stepResults;
    }

    public PlaceholderResolverPort getPlaceholderResolverPort() {
        return placeholderResolverPort;
    }
}
