package br.com.lucaslima.steprunner.adapters.http.out;

import java.util.Map;

public interface HttpClient {

    Object call(String method, String url, Map<String, String> headers, Object payload) throws Exception;
}
