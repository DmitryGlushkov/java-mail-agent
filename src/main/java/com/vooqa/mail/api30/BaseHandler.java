package com.vooqa.mail.api30;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.vooqa.mail.Configuration;

import java.io.IOException;

public abstract class BaseHandler implements HttpHandler {

    protected final Configuration configuration;

    public BaseHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        final String method = httpExchange.getRequestMethod();
        if ("POST".equals(method)) {
            try {
                if (doPost(httpExchange)) {
                    httpExchange.sendResponseHeaders(200, 0);
                    httpExchange.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                String error = e.getMessage() != null ? e.getMessage() : e.toString();
                byte[] respBody = error.getBytes();
                httpExchange.sendResponseHeaders(500, respBody.length);
                httpExchange.getResponseBody().write(respBody);
                httpExchange.close();
            }
        } else {
            byte[] respBody = "mail-agent: ON".getBytes();
            httpExchange.sendResponseHeaders(200, respBody.length);
            httpExchange.getResponseBody().write(respBody);
            httpExchange.close();
        }
    }

    protected abstract boolean doPost(HttpExchange httpExchange) throws Exception;

}
