package br.com.lucaslima.steprunner.application.exceptions;

public class HttpCallException extends WorkflowException {

    public HttpCallException(String method, String url, int statusCode, Throwable cause) {
        super(String.format("HTTP call failed: %s %s returned status code %d", method, url, statusCode), cause);
    }

}
