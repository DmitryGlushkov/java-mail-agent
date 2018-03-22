package com.vooqa.mail;

import com.sun.net.httpserver.HttpServer;
import com.vooqa.mail.api30.Api_30;

import java.net.InetSocketAddress;

public class Server {

    private final HttpServer server;

    public Server() throws Exception {
        server = HttpServer.create();
    }

    public void start(Configuration configuration) throws Exception {
        final Api_30 api = new Api_30(configuration);
        server.bind(new InetSocketAddress(configuration.serverPort()), 0);
        server.createContext("/api30/mail", api.mail());
        server.createContext("/api30/excel", api.excel());
        server.start();
    }

}
