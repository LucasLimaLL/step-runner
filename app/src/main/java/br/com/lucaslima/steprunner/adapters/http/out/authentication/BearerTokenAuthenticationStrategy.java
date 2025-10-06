package br.com.lucaslima.steprunner.adapters.http.out.authentication;

import br.com.lucaslima.steprunner.application.domains.ResolutionContext;
import br.com.lucaslima.steprunner.application.ports.out.AuthenticationStrategyPort;
import br.com.lucaslima.steprunner.application.ports.out.PlaceholderResolverPort;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class BearerTokenAuthenticationStrategy implements AuthenticationStrategyPort {
    private final PlaceholderResolverPort resolver;

    public BearerTokenAuthenticationStrategy(PlaceholderResolverPort resolver) {
        this.resolver = Objects.requireNonNull(resolver);
    }

    @Override
    public String strategyId() {
        return "BEARER";
    }

    @Override
    public Map<String, String> apply(String method, String url, Map<String, String> currentHeaders, Object payload, ResolutionContext ctx) {
        Map<String, String> out = new LinkedHashMap<>(currentHeaders);
        String token = resolver.resolveString("${Auth.token}", ctx);
        if (token != null && !token.isBlank()) {
            out.put("Authorization", "Bearer " + token);
        }
        return out;
    }
}
