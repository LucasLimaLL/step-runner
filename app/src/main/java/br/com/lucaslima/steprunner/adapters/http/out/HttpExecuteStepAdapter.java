package br.com.lucaslima.steprunner.adapters.http.out;

import br.com.lucaslima.steprunner.application.domains.ResolutionContext;
import br.com.lucaslima.steprunner.application.domains.Result;
import br.com.lucaslima.steprunner.application.domains.StepType;
import br.com.lucaslima.steprunner.application.domains.steps.HttpStep;
import br.com.lucaslima.steprunner.application.domains.steps.Step;
import br.com.lucaslima.steprunner.application.exceptions.AuthenticationTypeNotFoundException;
import br.com.lucaslima.steprunner.application.exceptions.HttpCallException;
import br.com.lucaslima.steprunner.application.exceptions.IllegalStepException;
import br.com.lucaslima.steprunner.application.ports.out.AuthenticationStrategyPort;
import br.com.lucaslima.steprunner.application.ports.out.ExecuteStepPort;
import br.com.lucaslima.steprunner.application.ports.out.PlaceholderResolverPort;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public Result execute(Step s, ResolutionContext resolutionContext) {

        if (!(s instanceof HttpStep step)) {
            throw new IllegalStepException("Step is not of type HttpStep");
        }
        String url = Optional.ofNullable(placeholderResolverPort.resolveString(step.getUrl(), resolutionContext))
                .orElseThrow(() -> new IllegalArgumentException("Resolved URL is null"));
        Object payloadResolved = placeholderResolverPort.resolveObject(step.getPayload(), resolutionContext);
        Map<String, Object> headersResolved = (Map<String, Object>) placeholderResolverPort.resolveObject(step.getHeaders(), resolutionContext);

        Map<String, String> headers = new LinkedHashMap<>();
        for (Map.Entry<String, Object> e : headersResolved.entrySet()) {
            headers.put(e.getKey(), e.getValue() == null ? null : String.valueOf(e.getValue()));
        }

        String strategyId = step.getAuthenticationStrategyId();
        AuthenticationStrategyPort strategy = authenticationStrategies.get(strategyId);
        if (strategy == null) { throw new AuthenticationTypeNotFoundException(strategyId); }

        Map<String, String> finalHeaders = strategy.apply(step.getMethod(), url, headers, payloadResolved, resolutionContext);

        try {
            Object responseBody = httpClient.call(step.getMethod(), url, finalHeaders, payloadResolved);
            Map<String, Object> resultData = new LinkedHashMap<>();
            resultData.put("url", url);
            resultData.put("response", responseBody);
            return Result.success(resultData, Map.of("httpMethod", step.getMethod(), "authStrategy", strategyId), "OK");
        } catch (Exception ex) {
            throw new HttpCallException(step.getMethod(), url, 500, ex);
        }
    }
}
