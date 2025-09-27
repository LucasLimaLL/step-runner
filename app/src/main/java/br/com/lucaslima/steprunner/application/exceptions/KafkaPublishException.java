package br.com.lucaslima.steprunner.application.exceptions;

public class KafkaPublishException extends WorkflowException {

    public KafkaPublishException(String topicName, Throwable cause) {
        super(String.format("Kafka publish failed for topic %s", topicName), cause);
    }

}