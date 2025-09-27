package br.com.lucaslima.steprunner.application.domains.steps;

import br.com.lucaslima.steprunner.application.domains.StepType;
import br.com.lucaslima.steprunner.application.domains.routing.Routing;

import java.util.HashMap;
import java.util.Map;

public final class SqsStep implements Step {

    private final String name;
    private final String queueName;
    private final Object payload;
    private final Map<String, Object> headers;
    private final Routing routing;

    public SqsStep(String name,
                   String queueName,
                   Object payload,
                   Map<String, Object> headers,
                   Routing routing) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name invalid");
        }
        if (queueName == null || queueName.isBlank()) {
            throw new IllegalArgumentException("queueName invalid");
        }
        this.name = name;
        this.queueName = queueName;
        this.payload = payload;
        this.headers = headers == null ? new HashMap<>() : headers;
        this.routing = routing;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public StepType getType() {
        return StepType.SQS;
    }

    @Override
    public Routing resultRouting() {
        return routing;
    }

    public String getQueueName() {
        return queueName;
    }

    public Object getPayload() {
        return payload;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

}
