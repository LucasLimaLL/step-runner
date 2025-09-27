package br.com.lucaslima.steprunner.application.exceptions;

public class SqsSendException extends WorkflowException {

    public SqsSendException(String queueName, Throwable cause) {
        super(String.format("Sqs send failed for queue %s", queueName), cause);
    }

}