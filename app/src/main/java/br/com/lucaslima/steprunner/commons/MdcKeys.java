package br.com.lucaslima.steprunner.commons;

public enum MdcKeys {

    CORRELATION_ID {
        @Override
        public String getKey() {
            return "correlationId";
        }
    },
    TRACE_ID {
        @Override
        public String getKey() {
            return "traceId";
        }
    };

    public abstract String getKey();
}
