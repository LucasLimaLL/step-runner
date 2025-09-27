package br.com.lucaslima.steprunner.application.domains.failure;

import java.util.Map;
import java.util.Optional;

public record FailureEnvelope(
        String name,
        String version,
        String lastExecutedStepName,
        Object workflowInput,
        String errorCode,
        String errorMessage,
        Map<String, Object> errorMetadata,
        Optional<String> traceId) {
}
