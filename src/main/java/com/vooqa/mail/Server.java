package com.vooqa.mail;

import com.sun.net.httpserver.HttpServer;
import com.vooqa.mail.api30.Api_30;
import java.net.InetSocketAddress;

public class Server {

    private final static Integer PORT = 81;

    private final HttpServer server;
    private final MailAgent agent;

    public Server(MailAgent mailAgent) throws Exception {
        agent = mailAgent;
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);
        server.createContext("api30/mail", Api_30.mail());
        server.createContext("api30/excel",  Api_30.excel());
    }

    public void start() {
        server.start();
    }

}
