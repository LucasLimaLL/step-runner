package br.com.lucaslima.steprunner.application.ports.out;

import br.com.lucaslima.steprunner.application.domains.ResolutionContext;

public interface PlaceholderResolverPort {

    String resolveString(String expression, ResolutionContext resolutionContext);
    Object resolveObject(Object expression, ResolutionContext resolutionContext);
}
