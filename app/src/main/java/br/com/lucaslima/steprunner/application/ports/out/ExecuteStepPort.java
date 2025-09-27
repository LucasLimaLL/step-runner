package br.com.lucaslima.steprunner.application.ports.out;

import br.com.lucaslima.steprunner.application.domains.ResolutionContext;
import br.com.lucaslima.steprunner.application.domains.Result;
import br.com.lucaslima.steprunner.application.domains.StepType;
import br.com.lucaslima.steprunner.application.domains.steps.Step;

public interface ExecuteStepPort {

    String STEP_SUCCESS_SENT = "SENT";
    String STEP_SUCCESS_OK = "OK";
    String STEP_FAILURE = "FAILED";
    StepType type();

    Result execute(Step step, ResolutionContext resolutionContext);
}
