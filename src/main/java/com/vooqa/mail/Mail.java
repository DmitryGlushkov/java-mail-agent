package com.vooqa.mail;

public class Mail {

    private static MailRuAgent mailRuAgent;

    public synchronized static MailRuAgent agent(Configuration configuration) {
        if (mailRuAgent == null) mailRuAgent = new MailRuAgent(configuration);
        return mailRuAgent;
    }
}
