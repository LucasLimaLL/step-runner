package br.com.lucaslima.steprunner.application.domains.retry;

public enum RetryPolicyType {

    RESUME_FROM_ROUTED_NEXT,
    REEXECUTE_LAST,
    RESUME_FROM_NAMED,
    REPLAY_FROM_FIRST_UNFINISHED
}
