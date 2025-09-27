package br.com.lucaslima.steprunner.application.ports.in;

import br.com.lucaslima.steprunner.application.domains.Workflow;

import java.util.Optional;

public interface GetWorkflowUseCase {

    Workflow getByNameAndVersion(String name, String version);
}
