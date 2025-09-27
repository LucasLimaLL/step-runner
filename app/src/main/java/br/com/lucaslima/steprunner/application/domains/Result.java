package br.com.lucaslima.steprunner.application.domains;

import java.util.HashMap;
import java.util.Map;

public final class Result {

    private static final String UNKNOWN = "UNKNOWN";
    private final boolean successfulExecution;
    private final Map<String, Object> resultData;
    private final Map<String, Object> metadataData;
    private final String statusValue;
    private final Object errorCause;

    public Result(boolean ok,
                  Map<String, Object> resultData,
                  Map<String, Object> metadataData,
                  String statusValue,
                  Object errorCause) {
        this.successfulExecution = ok;
        this.resultData = resultData == null ? new HashMap<>() : resultData;
        this.metadataData = metadataData == null ? new HashMap<>() : metadataData;
        this.statusValue = statusValue;
        this.errorCause = errorCause;
    }

    public static Result success(Map<String, Object> resultData, Map<String, Object> metadataData, String statusValue) {
        return new Result(true, resultData, metadataData, statusValue, null);
    }

    public static Result failure(Throwable exception, Map<String, Object> resultData, Map<String, Object> metadataData, String status) {
        return new Result(false, resultData, metadataData, status, exception == null ? UNKNOWN : exception.getLocalizedMessage());
    }

    public boolean isSuccessfulExecution() {
        return successfulExecution;
    }

    public Map<String, Object> getResultData() {
        return resultData;
    }

    public Map<String, Object> getMetadataData() {
        return metadataData;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public Object getErrorCause() {
        return errorCause;
    }
}
