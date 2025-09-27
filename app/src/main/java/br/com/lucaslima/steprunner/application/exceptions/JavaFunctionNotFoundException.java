package br.com.lucaslima.steprunner.application.exceptions;

public class JavaFunctionNotFoundException extends RuntimeException {
    public JavaFunctionNotFoundException(String functionId) {
        super(String.format("Function %s not found", functionId));
    }
}
