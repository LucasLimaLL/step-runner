package br.com.lucaslima.steprunner.adapters.persistence;

import br.com.lucaslima.steprunner.application.domains.Workflow;
import br.com.lucaslima.steprunner.application.ports.out.RetrieveWorkflowPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class RetrieveWorkflowAdapter implements RetrieveWorkflowPort {

    @Override
    public Optional<Workflow> retrieve(String name, String version) {
        return Optional.empty();
    }
}
