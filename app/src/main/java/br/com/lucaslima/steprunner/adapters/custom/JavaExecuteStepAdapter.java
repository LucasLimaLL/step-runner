package br.com.lucaslima.steprunner.adapters.custom;

import br.com.lucaslima.steprunner.application.domains.ResolutionContext;
import br.com.lucaslima.steprunner.application.domains.Result;
import br.com.lucaslima.steprunner.application.domains.StepType;
import br.com.lucaslima.steprunner.application.domains.steps.JavaStep;
import br.com.lucaslima.steprunner.application.domains.steps.Step;
import br.com.lucaslima.steprunner.application.exceptions.IllegalStepException;
import br.com.lucaslima.steprunner.application.exceptions.JavaFunctionExecutionException;
import br.com.lucaslima.steprunner.application.exceptions.JavaFunctionNotFoundException;
import br.com.lucaslima.steprunner.application.ports.out.ExecuteStepPort;
import br.com.lucaslima.steprunner.application.ports.out.JavaFunctionPort;
import br.com.lucaslima.steprunner.application.ports.out.PlaceholderResolverPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public final class JavaExecuteStepAdapter implements ExecuteStepPort {

    private final Map<String, JavaFunctionPort> registryFunctions;
    private final PlaceholderResolverPort placeholderResolverPort;

    public JavaExecuteStepAdapter(List<JavaFunctionPort> registryFunctions, PlaceholderResolverPort placeholderResolverPort) {
        this.placeholderResolverPort = placeholderResolverPort;
        this.registryFunctions = registryFunctions
                .stream()
                .collect(Collectors
                        .toMap(JavaFunctionPort::id, function -> function));
    }

    @Override
    public StepType type() {
        return StepType.JAVA;
    }

    @Override
    public Result execute(Step s, ResolutionContext resolutionContext) {

        if (!(s instanceof JavaStep step)) {
            throw new IllegalStepException("Step is not of type JavaStep");
        }

        JavaFunctionPort function = registryFunctions.get(step.getFunctionId());

        if (function == null) {
            throw new JavaFunctionNotFoundException(step.getFunctionId());
        }

        try {
            Map<String, Object> args = step.getArgs()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> placeholderResolverPort.resolveObject(e.getValue(), resolutionContext)
                    ));

            Object result = function.execute(args, resolutionContext);

            return Result.success(Map.of("success", result), Map.of(), STEP_SUCCESS_OK);
        } catch (Exception e) {
            throw new JavaFunctionExecutionException(step.getFunctionId(), e);
        }
    }
}
