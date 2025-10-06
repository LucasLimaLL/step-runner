package br.com.lucaslima.steprunner.adapters.http.out.authentication;

import br.com.lucaslima.steprunner.application.domains.ResolutionContext;
import br.com.lucaslima.steprunner.application.ports.out.AuthenticationStrategyPort;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DefaultAuthenticationStrategy implements AuthenticationStrategyPort {

    public static final String ID = "DEFAULT";

    @Override
    public String strategyId() {
        return ID;
    }

    @Override
    public Map<String, String> apply(String method, String url, Map<String, String> currentHeaders, Object payload, ResolutionContext context) {
        return new LinkedHashMap<>(currentHeaders);
    }
}
