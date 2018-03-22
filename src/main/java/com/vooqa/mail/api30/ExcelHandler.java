package com.vooqa.mail.api30;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.vooqa.mail.http.FormDataMgr;
import com.vooqa.mail.http.Part;

import java.io.IOException;
import java.util.List;

public class ExcelHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            final List<Part> parts = FormDataMgr.handle(httpExchange);
            if (parts != null && parts.size() == 2) {

            }

            httpExchange.sendResponseHeaders(200, 0);
            httpExchange.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
