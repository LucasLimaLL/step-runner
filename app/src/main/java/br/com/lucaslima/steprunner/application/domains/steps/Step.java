package br.com.lucaslima.steprunner.application.domains.steps;

import br.com.lucaslima.steprunner.application.domains.StepType;
import br.com.lucaslima.steprunner.application.domains.routing.Routing;

public sealed interface Step permits HttpStep, SqsStep, KafkaStep, JavaStep, TimeStep, SplitStep {

    String getName();

    StepType getType();

    default Routing resultRouting() {
        return null;
    }
}
