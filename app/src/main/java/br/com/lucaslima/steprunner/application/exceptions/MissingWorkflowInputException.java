package br.com.lucaslima.steprunner.application.exceptions;

public class MissingWorkflowInputException extends WorkflowException {
    public MissingWorkflowInputException(String workflowName) {
        super(String.format("Steps of %s is empty", workflowName));
    }
}
