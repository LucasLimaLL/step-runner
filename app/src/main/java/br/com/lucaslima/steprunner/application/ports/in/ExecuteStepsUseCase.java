package br.com.lucaslima.steprunner.application.ports.in;

import br.com.lucaslima.steprunner.application.domains.Request;
import br.com.lucaslima.steprunner.application.domains.ResolutionContext;
import br.com.lucaslima.steprunner.application.domains.Workflow;

public interface ExecuteStepsUseCase {

    void execute(Workflow workflow, Request executionRequest, ResolutionContext resolutionContext);
}
