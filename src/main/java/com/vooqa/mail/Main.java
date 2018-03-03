package com.vooqa.mail;

public class Main {

    public static void main(String[] args) throws Exception {
        MailAgent agent = new MailAgentImpl();
        new Server(agent).start();
    }

}
