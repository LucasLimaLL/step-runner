package br.com.lucaslima.steprunner.application.domains.steps;

import br.com.lucaslima.steprunner.application.domains.StepType;
import br.com.lucaslima.steprunner.application.domains.routing.Routing;

import java.util.Map;

public final class KafkaStep implements Step {

    private final String name;
    private final String topicName;
    private final Object messageKey;
    private final Object messageValue;
    private final Map<String, Object> headers;
    private final Routing routing;

    public KafkaStep(String name, String topicName, Object messageKey, Object messageValue, Map<String, Object> headers, Routing routing) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name invalid");
        }
        if (topicName == null || topicName.isBlank()) {
            throw new IllegalArgumentException("topicName invalid");
        }

        this.name = name;
        this.topicName = topicName;
        this.messageKey = messageKey;
        this.messageValue = messageValue;
        this.headers = headers;
        this.routing = routing;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public StepType getType() {
        return StepType.KAFKA;
    }

    @Override
    public Routing resultRouting() {
        return Step.super.resultRouting();
    }
}