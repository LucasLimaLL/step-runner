package br.com.lucaslima.steprunner.application.exceptions;

public class SplitListResolutionException extends RuntimeException {
    public SplitListResolutionException(String path) {
        super(String.format("Split list could not be resolved or ' %s ' is not a list", path));
    }
}
