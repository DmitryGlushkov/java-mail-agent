package com.vooqa.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class MailAgentImpl implements MailAgent {

    private final static Properties MAIL = new Properties();
    private final static Properties AUTH = new Properties();
    private final static Address[] RECIPIENTS = new InternetAddress[1];
    private static String LOGIN, PASSWORD;

    static {
        try {
            AUTH.load(MailAgentImpl.class.getResourceAsStream("/auth.properties"));
            MAIL.load(MailAgentImpl.class.getResourceAsStream("/mail.properties"));
            RECIPIENTS[0] = new InternetAddress("mobile.shift.llc@gmail.com");
            LOGIN = AUTH.getProperty("login");
            PASSWORD = AUTH.getProperty("pass");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final Session session;

    public MailAgentImpl() {
        session = Session.getInstance(MAIL);
        session.setDebug(false);
    }

    @Override
    public void sendEmail(String text) throws Exception {

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(LOGIN));
        message.setRecipients(Message.RecipientType.TO, RECIPIENTS);
        message.setSubject("KUB APP MESSAGE");
        message.setSentDate(new Date());
        message.setText(text, "UTF-8");
        message.saveChanges();

        Transport transport = session.getTransport("smtps");
        transport.connect(MAIL.getProperty("mail.smtps.host"), Integer.valueOf(MAIL.getProperty("mail.smtps.port")), LOGIN, PASSWORD);
        transport.sendMessage(message, RECIPIENTS);
    }
}
