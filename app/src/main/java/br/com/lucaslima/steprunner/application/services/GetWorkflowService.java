package br.com.lucaslima.steprunner.application.services;

import br.com.lucaslima.steprunner.application.domains.Workflow;
import br.com.lucaslima.steprunner.application.exceptions.WorkflowNotFoundException;
import br.com.lucaslima.steprunner.application.ports.in.GetWorkflowUseCase;
import br.com.lucaslima.steprunner.application.ports.out.RetrieveWorkflowPort;

import javax.swing.text.html.Option;
import java.util.Optional;


public class GetWorkflowService implements GetWorkflowUseCase {

    private final RetrieveWorkflowPort retrieveWorkflowPort;

    public GetWorkflowService(RetrieveWorkflowPort retrieveWorkflowPort) {
        this.retrieveWorkflowPort = retrieveWorkflowPort;
    }

    @Override
    public Workflow getByNameAndVersion(String name, String version) {

        Optional<Workflow> workflowOptional = retrieveWorkflowPort.retrieve(name, version);

        if (workflowOptional.isEmpty()) {
            throw new WorkflowNotFoundException(name);
        }

        return workflowOptional.get();
    }
}
