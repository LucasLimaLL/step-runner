package br.com.lucaslima.steprunner.application.ports.out;

import br.com.lucaslima.steprunner.application.domains.ResolutionContext;

import java.util.Map;

public interface JavaFunctionPort {

    String id();

    Object execute(Map<String, Object> args, ResolutionContext resolutionContext);
}
