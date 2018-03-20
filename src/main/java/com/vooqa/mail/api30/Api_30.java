package com.vooqa.mail.api30;

import com.sun.net.httpserver.HttpHandler;

public class Api_30 {

    public static HttpHandler mail(){
        return new MailHandler();
    }

    public static HttpHandler excel(){
        return new ExcelHandler();
    }
}
