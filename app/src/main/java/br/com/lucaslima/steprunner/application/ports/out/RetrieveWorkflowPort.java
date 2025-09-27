package br.com.lucaslima.steprunner.application.ports.out;

import br.com.lucaslima.steprunner.application.domains.Workflow;

import java.util.Optional;

public interface RetrieveWorkflowPort {

    Optional<Workflow> retrieve(String name, String version);
}
