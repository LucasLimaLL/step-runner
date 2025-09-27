package br.com.lucaslima.steprunner.application.ports.out;

import br.com.lucaslima.steprunner.application.domains.ResolutionContext;

public interface ExpressionEvaluatorPort {

    boolean evaluate(String expression, ResolutionContext resolutionContext, PlaceholderResolverPort placeholderResolverPort);
}
