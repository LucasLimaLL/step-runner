package br.com.lucaslima.steprunner.application.exceptions;

public class RetryStalledException extends RuntimeException {
    public RetryStalledException(String stepName) {
        super(String.format("Retry stalled at step: %s", stepName));
    }
}
