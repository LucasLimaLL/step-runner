package br.com.lucaslima.steprunner.application.exceptions;

public class IllegalStepException extends WorkflowException {
    public IllegalStepException(String message) {
        super(message);
    }
}
