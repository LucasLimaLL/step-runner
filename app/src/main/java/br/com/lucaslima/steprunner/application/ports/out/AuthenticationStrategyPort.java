package br.com.lucaslima.steprunner.application.ports.out;

import br.com.lucaslima.steprunner.application.domains.ResolutionContext;

import java.util.Map;

public interface AuthenticationStrategyPort {
    String strategyId();

    Map<String, String> apply(String method, String url, Map<String, String> currentHeaders, Object payload, ResolutionContext context);
}
