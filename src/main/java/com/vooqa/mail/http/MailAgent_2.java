package com.vooqa.mail.http;

import javax.mail.*;
import javax.mail.internet.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Properties;

public class MailAgent_2 {

    private String LOGIN = "kubaturnik_app@mail.ru";
    private String PASS = "Rjatdnehrt";

    private final static String HOST = "smtp.mail.ru";
    private final static String PROTO = "smtps";
    private final static Integer PORT = 465;

    private final Session session;

    public MailAgent_2() {
        final Properties properties = new Properties();
        properties.put("mail.smtps.host", HOST);
        properties.put("mail.smtps.port", PORT);
        properties.put("mail.smtps.auth", "true");
        properties.put("mail.smtps.ssl.socketFactory", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtps.ssl.port", PORT);
        properties.put("mail.transport.protocol", PROTO);
        session = Session.getInstance(properties);
        session.setDebug(true);

    }

    public void sendMail(String to, String attachementName, byte[] attachement) throws Exception {
        Address[] recipients = new Address[]{new InternetAddress(to)};
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(LOGIN));
        message.setRecipients(Message.RecipientType.TO, recipients);
        message.setSubject("KUB APP MESSAGE");
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
        Transport transport = session.getTransport(PROTO);
        transport.connect(HOST, PORT, LOGIN, PASS);
        transport.sendMessage(message, recipients);
    }

    private String encode(String src) throws Exception{
       return URLEncoder.encode(src, "UTF-8");
    }

}
