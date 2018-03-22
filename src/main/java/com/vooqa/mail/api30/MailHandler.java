package com.vooqa.mail.api30;

import com.sun.net.httpserver.HttpExchange;
import com.vooqa.mail.Configuration;
import com.vooqa.mail.Mail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MailHandler extends BaseHandler {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public MailHandler(Configuration configuration) {
        super(configuration);
    }

    @Override
    protected void doPost(HttpExchange httpExchange) throws Exception {
        Map<String, String> parameters = readParameters(httpExchange.getRequestBody());
        if (parameters != null && parameters.size() > 0) {
            Mail.agent(configuration).sendMessage(printEmailText(parameters));
        }
    }

    private Map<String, String> readParameters(InputStream in) throws Exception {
        Map<String, String> result = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String data = "";
        String line;
        while ((line = reader.readLine()) != null) {
            data += line;
        }
        String[] split = data.split("&");
        for (String pair : split) {
            String[] _pair = pair.split("=");
            if (_pair.length == 2) {
                result.put(URLDecoder.decode(_pair[0], "UTF-8").trim(), URLDecoder.decode(_pair[1], "UTF-8").trim());
            }
        }
        return result;
    }

    private String printEmailText(Map<String, String> params) {

        String text  = params.get("text");  text  = text != null ? text : "-";
        String name  = params.get("name");  name  = name != null ? name : "-";
        String mail  = params.get("mail");  mail  = mail != null ? mail : "-";
        String gmail = params.get("gmail"); gmail = gmail != null ? gmail : "-";

        String date = sdf.format(new Date());

        return String.format("" +
                        "%-20s %s\n" +
                        "%-20s %s\n" +
                        "%-20s %s\n" +
                        "%-20s %s\n" +
                        "%-20s %s\n",
                "Дата:", date,
                "Имя:", name,
                "Почта(1):", mail,
                "Почта(2):", gmail,
                "Сообщение:", text);

    }

}
