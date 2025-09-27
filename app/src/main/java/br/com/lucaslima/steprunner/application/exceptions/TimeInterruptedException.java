package br.com.lucaslima.steprunner.application.exceptions;

public class TimeInterruptedException extends WorkflowException {

    public TimeInterruptedException(long duration, Throwable cause) {
        super(String.format("Sleep interrupted after %d milliseconds", duration), cause);
    }

}
