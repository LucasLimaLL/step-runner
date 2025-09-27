package br.com.lucaslima.steprunner.adapters.http;

import br.com.lucaslima.steprunner.application.domains.ResolutionContext;
import br.com.lucaslima.steprunner.application.domains.Result;
import br.com.lucaslima.steprunner.application.domains.StepType;
import br.com.lucaslima.steprunner.application.domains.steps.Step;
import br.com.lucaslima.steprunner.application.ports.out.AuthenticationStrategyPort;
import br.com.lucaslima.steprunner.application.ports.out.ExecuteStepPort;
import br.com.lucaslima.steprunner.application.ports.out.PlaceholderResolverPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public final class HttpExecuteStepAdapter implements ExecuteStepPort {

    private final HttpClient httpClient;
    private final PlaceholderResolverPort placeholderResolverPort;
    private final Map<String, AuthenticationStrategyPort> authenticationStrategies;

    public HttpExecuteStepAdapter(HttpClient httpClient, PlaceholderResolverPort placeholderResolverPort, List<AuthenticationStrategyPort> authenticationStrategies) {
        this.httpClient = httpClient;
        this.placeholderResolverPort = placeholderResolverPort;
        this.authenticationStrategies = authenticationStrategies
                .stream()
                .collect(
                        Collectors.toMap(
                                AuthenticationStrategyPort::strategyId,
                                step -> step,
                                (existing, duplicate) -> existing
                        ));
    }

    @Override
    public StepType type() {
        return StepType.HTTP;
    }

    @Override
    public Result execute(Step step, ResolutionContext resolutionContext) {
        return null;
    }
}
