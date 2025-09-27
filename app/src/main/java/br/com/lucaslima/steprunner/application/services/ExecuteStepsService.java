package br.com.lucaslima.steprunner.application.services;

import br.com.lucaslima.steprunner.application.domains.Request;
import br.com.lucaslima.steprunner.application.domains.ResolutionContext;
import br.com.lucaslima.steprunner.application.domains.Result;
import br.com.lucaslima.steprunner.application.domains.StepType;
import br.com.lucaslima.steprunner.application.domains.Workflow;
import br.com.lucaslima.steprunner.application.domains.steps.Step;
import br.com.lucaslima.steprunner.application.exceptions.IllegalContextException;
import br.com.lucaslima.steprunner.application.exceptions.IllegalStepException;
import br.com.lucaslima.steprunner.application.exceptions.IllegalWorkflowException;
import br.com.lucaslima.steprunner.application.ports.in.ExecuteStepsUseCase;
import br.com.lucaslima.steprunner.application.ports.out.ExecuteStepPort;
import br.com.lucaslima.steprunner.application.ports.out.ExpressionEvaluatorPort;
import br.com.lucaslima.steprunner.application.ports.out.PlaceholderResolverPort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ExecuteStepsService implements ExecuteStepsUseCase {

    private static final String WORKFLOW_INPUT_ID = "workflowInput";
    private final Map<StepType, ExecuteStepPort> mapSteps;
    private final PlaceholderResolverPort placeholderResolverPort;
    private final ExpressionEvaluatorPort expressionEvaluatorPort;

    public ExecuteStepsService(List<ExecuteStepPort> steps, ExpressionEvaluatorPort expressionEvaluatorPort, PlaceholderResolverPort placeholderResolverPort) {
        this.mapSteps = steps
                .stream()
                .collect(
                        Collectors.toMap(
                                ExecuteStepPort::type,
                                step -> step,
                                (existing, duplicate) -> existing
                        ));

        this.expressionEvaluatorPort = expressionEvaluatorPort;
        this.placeholderResolverPort = placeholderResolverPort;
    }

    @Override
    public void execute(Workflow workflow, Request executionRequest, ResolutionContext resolutionContext) {

        validateData(workflow, executionRequest, resolutionContext);

        executionRequest
                .workflowInput()
                .ifPresent(input -> saveContext(resolutionContext, WORKFLOW_INPUT_ID, input));

        var mapStepsByName = mapStepsByName(workflow.steps());
        var startStep = getFirstStep(workflow.steps(), executionRequest.lastExecutedStepName());

        execute(mapStepsByName, startStep, resolutionContext);
    }

    private void execute(Map<String, Step> mapStepsByName, String startStep, ResolutionContext resolutionContext) {
        String currentStep = startStep;

        while (currentStep != null) {
            var step = mapStepsByName.get(currentStep);
            Result result = executeStep(step, resolutionContext);
            saveContext(resolutionContext, step.getName(), result.getResultData());

            currentStep = decideNextStep(step, result, resolutionContext);
        }
    }

    private String decideNextStep(Step step, Result result, ResolutionContext resolutionContext) {

        var routingSpecification = step.resultRouting();

        if (routingSpecification == null) {
            return null;
        }

        if (routingSpecification.routingRules() == null || routingSpecification.routingRules().isEmpty()) {
            return result.isSuccessfulExecution() ? routingSpecification.defaultSucceedStepName() : routingSpecification.defaultFailStepName();
        }

        var route = routingSpecification
                .routingRules()
                .stream()
                .filter(rule -> {
                    String expression = rule.whenExpresion();

                    if (expression != null && !expression.isBlank()) {
                        return expressionEvaluatorPort.evaluate(expression, resolutionContext, placeholderResolverPort);
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);

        return route == null ? null : route.nextStepName();

    }

    private Result executeStep(Step step, ResolutionContext resolutionContext) {
        ExecuteStepPort executor = mapSteps.get(step.getType());
        if (executor == null) {
            throw new IllegalStepException("No executor found for step type: " + step.getType());
        }

        return executor.execute(step, resolutionContext);
    }


    private String getFirstStep(List<Step> steps, Optional<String> lastExecutedStepName) {
        if (steps == null || steps.isEmpty()) {
            return null;
        }

        return lastExecutedStepName.isEmpty()
                ? steps.getFirst().getName() :
                steps
                        .stream()
                        .filter(step -> step.getName().equals(lastExecutedStepName.get()))
                        .findFirst()
                        .map(Step::getName)
                        .orElse(null);
    }

    private void validateData(Workflow workflow, Request executionRequest, ResolutionContext resolutionContext) {
        if (workflow == null || workflow.steps() == null || workflow.steps().isEmpty()) {
            throw new IllegalWorkflowException("Workflow or steps cannot be null or empty");
        }

        if (resolutionContext == null) {
            throw new IllegalContextException("Resolution context cannot be null");
        }

        if (executionRequest == null || executionRequest.workflowInput().isEmpty()) {
            throw new IllegalContextException("Execution request or input cannot be null");
        }
    }

    private Map<String, Step> mapStepsByName(List<Step> steps) {
        return steps
                .stream()
                .collect(Collectors.toMap(Step::getName, step -> step, (existing, duplicate) -> existing));
    }

    private void saveContext(ResolutionContext resolutionContext, String key, Object input) {
        resolutionContext
                .getStepResults()
                .put(key, input);
    }
}
