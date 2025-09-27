package br.com.lucaslima.steprunner.adapters.time;

import br.com.lucaslima.steprunner.application.domains.ResolutionContext;
import br.com.lucaslima.steprunner.application.domains.Result;
import br.com.lucaslima.steprunner.application.domains.StepType;
import br.com.lucaslima.steprunner.application.domains.steps.Step;
import br.com.lucaslima.steprunner.application.domains.steps.TimeStep;
import br.com.lucaslima.steprunner.application.exceptions.TimeInterruptedException;
import br.com.lucaslima.steprunner.application.ports.out.ExecuteStepPort;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public final class TimeExecuteStepAdapter implements ExecuteStepPort {

    @Override
    public StepType type() {
        return StepType.TIME;
    }

    @Override
    public Result execute(Step s, ResolutionContext resolutionContext) {

        TimeStep step = (TimeStep) s;

        try {
            Thread.sleep(step.getDurationMs());
            return Result.success(Map.of("sleptTimeInMs", step.getDurationMs()), Map.of(), STEP_SUCCESS_OK);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new TimeInterruptedException(step.getDurationMs(), ex);
        }
    }
}
