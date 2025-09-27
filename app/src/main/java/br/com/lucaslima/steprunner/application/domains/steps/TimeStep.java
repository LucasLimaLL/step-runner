package br.com.lucaslima.steprunner.application.domains.steps;

import br.com.lucaslima.steprunner.application.domains.StepType;
import br.com.lucaslima.steprunner.application.domains.routing.Routing;

public final class TimeStep implements Step {

    private final String name;
    private final long durationMs;
    private final Routing routing;

    public TimeStep(String name, long durationMs, Routing routing) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name invalid");
        }

        this.name = name;
        this.durationMs = durationMs;
        this.routing = routing;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public StepType getType() {
        return StepType.TIME;
    }

    @Override
    public Routing resultRouting() {
        return routing;
    }

    public long getDurationMs() {
        return durationMs;
    }

}
