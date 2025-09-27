package br.com.lucaslima.steprunner.application.domains.routing;

public record RoutingRule(String whenExpresion, String nextStepName) {
}
