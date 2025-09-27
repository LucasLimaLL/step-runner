package br.com.lucaslima.steprunner.application.exceptions;

public class JavaFunctionExecutionException extends WorkflowException {

    public JavaFunctionExecutionException(String functionId, Throwable cause) {
        super(String.format("Execution failed for function %s", functionId), cause);
    }

}