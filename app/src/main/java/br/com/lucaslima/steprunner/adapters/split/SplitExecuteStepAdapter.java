package br.com.lucaslima.steprunner.adapters.split;

import br.com.lucaslima.steprunner.application.domains.ResolutionContext;
import br.com.lucaslima.steprunner.application.domains.Result;
import br.com.lucaslima.steprunner.application.domains.StepType;
import br.com.lucaslima.steprunner.application.domains.Workflow;
import br.com.lucaslima.steprunner.application.domains.steps.HttpStep;
import br.com.lucaslima.steprunner.application.domains.steps.SplitStep;
import br.com.lucaslima.steprunner.application.domains.steps.Step;
import br.com.lucaslima.steprunner.application.exceptions.IllegalStepException;
import br.com.lucaslima.steprunner.application.exceptions.SplitListResolutionException;
import br.com.lucaslima.steprunner.application.ports.out.ExecuteStepPort;
import br.com.lucaslima.steprunner.application.ports.out.PlaceholderResolverPort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public final class SplitExecuteStepAdapter implements ExecuteStepPort {

    private final PlaceholderResolverPort placeholderResolverPort;

    public SplitExecuteStepAdapter(PlaceholderResolverPort placeholderResolverPort) {
        this.placeholderResolverPort = placeholderResolverPort;
    }

    @Override
    public StepType type() {
        return StepType.SPLIT;
    }

    @Override
    public Result execute(Step s, ResolutionContext resolutionContext) {

        if (!(s instanceof SplitStep step)) {
            throw new IllegalStepException("Step is not of type SplitStep");
        }
        
        Object resolvedList = placeholderResolverPort.resolveObject(step.getListPath(), resolutionContext);

        if (!(resolvedList instanceof List<?> items)) {
            throw new SplitListResolutionException(String.valueOf(step.getListPath()));
        }

        List<Object> results = new ArrayList<>();
        int index = 0;
        for (Object item : items) {
            resolutionContext.getStepResults().put("$item", item);
            resolutionContext.getStepResults().put("$index", index);


            resolutionContext.getStepResults().remove("$item");
            resolutionContext.getStepResults().remove("$index");
            index++;
        }

        Map<String, Object> resultData = Map.of("results", results);
        Map<String, Object> metadataData = Map.of("count", results.size());
        return Result.success(resultData, metadataData, "OK");

    }
}
