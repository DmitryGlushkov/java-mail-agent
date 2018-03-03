package com.vooqa.mail;

public interface MailAgent {
    void sendEmail(String text) throws Exception;
}
