package br.com.lucaslima.steprunner.application.domains.retry;

public record RetryOptions(
        RetryPolicyType type,
        String resumeStepName,
        boolean allowReexcuteSameStep
) {
}
