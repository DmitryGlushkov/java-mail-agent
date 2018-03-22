package com.vooqa.mail.api30;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.vooqa.mail.http.FormDataMgr;
import com.vooqa.mail.http.MailAgent_2;
import com.vooqa.mail.http.Part;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class ExcelHandler implements HttpHandler {

    private final static boolean saveFile = false;
    private final static String DIR = "D:\\tmp\\";

    private final MailAgent_2 ma = new MailAgent_2();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            final Map<String, Part> parts = FormDataMgr.handle(httpExchange);
            if (parts != null && parts.size() == 2) {
                String recipient = parts.containsKey("email") ? new String(parts.get("email").data) : null;
                Part filePart = parts.get("file");
                if (filePart != null && filePart.data != null) {
                    if (saveFile) setSaveFile(filePart);
                    ma.sendMail(recipient, filePart.filename, filePart.data);
                }
            }
            httpExchange.sendResponseHeaders(200, 0);
            httpExchange.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSaveFile(Part filePart) throws Exception {
        String fileName = filePart.filename != null && !filePart.filename.isEmpty() ? filePart.filename : "the_file";
        FileOutputStream outputStream = new FileOutputStream(DIR + fileName);
        outputStream.write(filePart.data);
        outputStream.flush();
        outputStream.close();
    }
}
