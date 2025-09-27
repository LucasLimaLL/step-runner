package br.com.lucaslima.steprunner.application.domains.steps;

import br.com.lucaslima.steprunner.application.domains.StepType;
import br.com.lucaslima.steprunner.application.domains.routing.Routing;

import java.util.List;

public final class SplitStep implements Step {

    private final String name;
    private final String listPath;
    private final List<Step> subSteps;
    private final Routing routing;

    public SplitStep(String name, String listPath, List<Step> subSteps, Routing routing) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name invalid");
        }

        if (listPath == null || listPath.isBlank()) {
            throw new IllegalArgumentException("listPath invalid");
        }

        this.name = name;
        this.listPath = listPath;
        this.subSteps = subSteps;
        this.routing = routing;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public StepType getType() {
        return StepType.SPLIT;
    }

    @Override
    public Routing resultRouting() {
        return routing;
    }

    public String getListPath() {
        return listPath;
    }

    public List<Step> getSubSteps() {
        return subSteps;
    }
}
