package br.com.lucaslima.steprunner.application.domains.routing;

import java.util.List;

public record Routing(List<RoutingRule> routingRules, String defaultSucceedStepName, String defaultFailStepName) {
}
