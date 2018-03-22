package com.vooqa.mail;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

public class MailRuAgent {

    private final Session session;
    private final String login;
    private final String password;
    private final String smtpHost;
    private final Integer smtpPort;
    private final String adminMailBox;

    public MailRuAgent(Configuration configuration) {
        this.adminMailBox = configuration.adminMailBox();
        this.smtpHost = configuration.smtpHost();
        this.smtpPort = configuration.smtpPort();
        this.login = configuration.mailBox();
        this.password = configuration.mailBoxPassword();
        this.session = Session.getInstance(getSessionProperties(smtpHost, smtpPort));
        session.setDebug(false);
    }

    private Properties getSessionProperties(String host, Integer port) {
        final Properties properties = new Properties();
        properties.put("mail.smtps.host", host);
        properties.put("mail.smtps.port", port);
        properties.put("mail.smtps.auth", "true");
        properties.put("mail.smtps.ssl.socketFactory", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtps.ssl.port", port);
        properties.put("mail.transport.protocol", "smtps");
        return properties;
    }

    public void sendMessage(String text) throws Exception {
        _mail(adminMailBox, text, "KUB APP MESSAGE", null, null);
    }

    public void sendFile(String to, String fileName, byte[] fileBody) throws Exception {
        _mail(to, fileName, null, fileName, fileBody);
    }

    private void _mail(String to, String subject, String text, String attachementName, byte[] attachement) throws Exception {
        Address[] recipients = new Address[]{new InternetAddress(to)};
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(login));
        message.setRecipients(Message.RecipientType.TO, recipients);
        message.setSubject(subject);
        if (text != null) message.setText(text, "UTF-8");
        message.setSentDate(new Date());
        if (attachement != null) {
            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(attachement, "application/vnd.ms-excel");
            bodyPart.setFileName(MimeUtility.encodeText(attachementName));
            multipart.addBodyPart(bodyPart);
            message.setContent(multipart);
        }
        message.saveChanges();

        Transport transport = session.getTransport("smtps");
        transport.connect(smtpHost, smtpPort, login, password);
        transport.sendMessage(message, recipients);
    }
}
