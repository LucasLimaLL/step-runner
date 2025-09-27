package br.com.lucaslima.steprunner.application.domains.steps;

import br.com.lucaslima.steprunner.application.domains.StepType;
import br.com.lucaslima.steprunner.application.domains.routing.Routing;

import java.util.Map;

public final class JavaStep implements Step {

    private final String name;
    private final String functionId;
    private final Map<String, Object> args;
    private final Routing routing;

    public JavaStep(String name, String functionId, Map<String, Object> args, Routing routing) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name invalid");
        }

        if (functionId == null || functionId.isBlank()) {
            throw new IllegalArgumentException("functionId invalid");
        }

        this.name = name;
        this.functionId = functionId;
        this.args = args;
        this.routing = routing;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public StepType getType() {
        return StepType.JAVA;
    }

    @Override
    public Routing resultRouting() {
        return routing;
    }

    public String getFunctionId() {
        return functionId;
    }

    public Map<String, Object> getArgs() {
        return args;
    }
}
