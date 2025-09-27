package br.com.lucaslima.steprunner.application.exceptions;

public class AuthenticationTypeNotFoundException extends WorkflowException {
    public AuthenticationTypeNotFoundException(String authenticationType) {
        super(String.format("Authentication type %s not found", authenticationType));
    }
}
