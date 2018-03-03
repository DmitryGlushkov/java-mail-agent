package com.vooqa.mail;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private final static String PATH = "/mail";
    private final static Integer PORT = 81;

    private final HttpServer server;
    private final MailAgent agent;

    public Server(MailAgent mailAgent) throws Exception {
        agent = mailAgent;
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);
        server.createContext(PATH, new MailHandler());
    }

    public void start() {
        server.start();
    }

    private class MailHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            final String method = httpExchange.getRequestMethod();
            if ("POST".equals(method)) {
                Map<String, String> parameters = null;
                try {
                    parameters = readParameters(httpExchange.getRequestBody());
                } catch (Exception e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(500, 0);
                    return;
                }
                if (parameters != null && parameters.size() > 0) {
                    try {
                        agent.sendEmail(printEmailText(parameters));
                    } catch (Exception e){
                        System.out.println(String.format("[%s] EXCEPTION ------------------------- ", sdf.format(new Date())));
                        e.printStackTrace();
                        String error = e.getMessage() != null ? e.getMessage() : e.toString();
                        byte[] respBody = error.getBytes();
                        httpExchange.sendResponseHeaders(500, respBody.length);
                        httpExchange.getResponseBody().write(respBody);
                        httpExchange.close();
                        return;
                    }
                }
                httpExchange.sendResponseHeaders(200, 0);
                httpExchange.close();
                return;
            } else if ("GET".equals(method)) {
                byte[] respBody = "mail-agent: ON".getBytes();
                httpExchange.sendResponseHeaders(200, respBody.length);
                httpExchange.getResponseBody().write(respBody);
                httpExchange.close();
            }
            httpExchange.sendResponseHeaders(404, 0);
        }

        private Map<String, String> readParameters(InputStream in) throws Exception {
            Map<String, String> result = new HashMap<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String data = "";
            String line;
            while ((line = reader.readLine()) != null) {
                data += line;
            }
            String[] split = data.split("&");
            for (String pair : split) {
                String[] _pair = pair.split("=");
                if (_pair.length == 2) {
                    result.put(URLDecoder.decode(_pair[0], "UTF-8").trim(), URLDecoder.decode(_pair[1], "UTF-8").trim());
                }
            }
            return result;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        private String printEmailText(Map<String, String> params) {

            String text  = params.get("text");  text  = text != null ? text : "-";
            String name  = params.get("name");  name  = name != null ? name : "-";
            String mail  = params.get("mail");  mail  = mail != null ? mail : "-";
            String gmail = params.get("gmail"); gmail = gmail != null ? gmail : "-";

            String date = sdf.format(new Date());

            return String.format("" +
                    "%-20s %s\n" +
                    "%-20s %s\n" +
                    "%-20s %s\n" +
                    "%-20s %s\n" +
                    "%-20s %s\n",
                    "Дата:", date,
                    "Имя:", name,
                    "Почта(1):", mail,
                    "Почта(2):", gmail,
                    "Сообщение:", text);

        }
    }
}
