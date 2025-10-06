package br.com.lucaslima.steprunner.adapters.sqs;

import br.com.lucaslima.steprunner.application.domains.ResolutionContext;
import br.com.lucaslima.steprunner.application.domains.Result;
import br.com.lucaslima.steprunner.application.domains.StepType;
import br.com.lucaslima.steprunner.application.domains.steps.SplitStep;
import br.com.lucaslima.steprunner.application.domains.steps.SqsStep;
import br.com.lucaslima.steprunner.application.domains.steps.Step;
import br.com.lucaslima.steprunner.application.exceptions.IllegalStepException;
import br.com.lucaslima.steprunner.application.ports.out.ExecuteStepPort;
import br.com.lucaslima.steprunner.application.ports.out.PlaceholderResolverPort;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public final class SqsExecuteStepAdapter implements ExecuteStepPort {

    private static final String EMPTY_DEFAULT = "";
    private final SqsClient sqsClient;
    private final PlaceholderResolverPort placeholderResolverPort;

    public SqsExecuteStepAdapter(SqsClient sqsClient, PlaceholderResolverPort placeholderResolverPort) {
        this.sqsClient = sqsClient;
        this.placeholderResolverPort = placeholderResolverPort;
    }

    @Override
    public StepType type() {
        return StepType.SQS;
    }

    @Override
    public Result execute(Step s, ResolutionContext resolutionContext) {

        if (!(s instanceof SqsStep step)) {
            throw new IllegalStepException("Step is not of type SqsStep");
        }

        String queueName = Optional.ofNullable(placeholderResolverPort.resolveString(step.getQueueName(), resolutionContext))
                .orElseThrow(() -> new IllegalArgumentException("queueName is required"));
        Object payload = placeholderResolverPort.resolveObject(step.getPayload(), resolutionContext);
        String messageBody = Optional.ofNullable(payload).map(String::valueOf).orElse(EMPTY_DEFAULT);

        try {
            String queueUrl = sqsClient.getQueueUrl(
                    GetQueueUrlRequest.builder().queueName(queueName).build()
            ).queueUrl();

            Map<String, MessageAttributeValue> messageAttributes =
                    step
                            .getHeaders()
                            .entrySet()
                            .stream()
                            .collect(
                                    Collectors.toMap(
                                            Map.Entry::getKey,
                                            e -> MessageAttributeValue.builder()
                                                    .stringValue(String.valueOf(e.getValue()))
                                                    .dataType("String")
                                                    .build()
                                    ));

            SendMessageResponse response = sqsClient.sendMessage(
                    SendMessageRequest
                            .builder()
                            .queueUrl(queueUrl)
                            .messageBody(messageBody)
                            .messageAttributes(messageAttributes)
                            .build());

            if (response.sdkHttpResponse().isSuccessful()) {
                return Result.success(
                        Map.of(
                                "messageId", response.messageId(),
                                "md5OfMessageBody", response.md5OfMessageBody(),
                                "queueUrl", queueUrl,
                                "queueName", queueName,
                                "body", messageBody
                        ), step.getHeaders(), STEP_SUCCESS_SENT);
            } else {
                return Result.failure(new Exception("Failed to send message to SQS"), Map.of(), step.getHeaders(), STEP_FAILURE);
            }
        } catch (Exception e) {
            return Result.failure(e, Map.of(), step.getHeaders(), STEP_FAILURE);
        }
    }
}
