package com.vooqa.mail.api30;

import com.sun.net.httpserver.HttpHandler;
import com.vooqa.mail.Configuration;

public class Api_30 {

    // application/x-www-form-urlencoded
    // params: text; name; mail; gmail
    private final HttpHandler hMail;

    // multipart/form-data
    // parts: file(excel); email(recipient)
    private final HttpHandler hExcel;

    public Api_30(Configuration configuration) {
        this.hMail = new MailHandler(configuration);
        this.hExcel = new ExcelHandler(configuration);
    }

    public HttpHandler mail() {
        return hMail;
    }

    public HttpHandler excel() {
        return hExcel;
    }
}
