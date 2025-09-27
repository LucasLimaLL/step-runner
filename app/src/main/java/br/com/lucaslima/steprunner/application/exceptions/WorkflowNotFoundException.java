package br.com.lucaslima.steprunner.application.exceptions;

public class WorkflowNotFoundException extends WorkflowException {
    public WorkflowNotFoundException(String workflowName) {
        super(String.format("Workflow %s not found", workflowName));
    }
}
