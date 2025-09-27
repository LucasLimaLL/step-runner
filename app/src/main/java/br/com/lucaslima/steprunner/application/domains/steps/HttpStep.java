package br.com.lucaslima.steprunner.application.domains.steps;

import br.com.lucaslima.steprunner.application.domains.StepType;
import br.com.lucaslima.steprunner.application.domains.routing.Routing;

import java.util.HashMap;
import java.util.Map;

public final class HttpStep implements Step {

    private final String name;
    private final String url;
    private final String method;
    private final String authenticationStrategyId;
    private final Map<String, Object> headers;
    private final Object payload;
    private final Routing routing;

    public HttpStep(
            String name,
            String url,
            String method,
            String authenticationStrategyId,
            Map<String, Object> headers,
            Object payload,
            Routing routing) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name invalid");
        }

        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("url invalid");
        }

        if (method == null || method.isBlank()) {
            throw new IllegalArgumentException("method invalid");
        }

        if (authenticationStrategyId == null || authenticationStrategyId.isBlank()) {
            authenticationStrategyId = "DEFAULT";
        }

        if (headers == null) {
            headers = new HashMap<>();
        }

        this.name = name;
        this.url = url;
        this.method = method;
        this.authenticationStrategyId = authenticationStrategyId;
        this.headers = headers;
        this.payload = payload;
        this.routing = routing;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public StepType getType() {
        return StepType.HTTP;
    }

    @Override
    public Routing resultRouting() {
        return routing;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getAuthenticationStrategyId() {
        return authenticationStrategyId;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public Object getPayload() {
        return payload;
    }
}
