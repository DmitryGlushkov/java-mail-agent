package com.vooqa.mail.api30;

import com.sun.net.httpserver.HttpExchange;
import com.vooqa.mail.Configuration;
import com.vooqa.mail.Mail;
import com.vooqa.mail.http.FormDataMgr;
import com.vooqa.mail.http.Part;

import java.io.FileOutputStream;
import java.util.Map;

public class ExcelHandler extends BaseHandler {

    public ExcelHandler(Configuration configuration) {
        super(configuration);
    }

    @Override
    protected void doPost(HttpExchange httpExchange) throws Exception {
        final Map<String, Part> parts = FormDataMgr.handle(httpExchange);
        if (parts != null && parts.size() == 2) {
            String recipient = parts.containsKey("email") ? new String(parts.get("email").data) : null;
            Part filePart = parts.get("file");
            if (filePart != null && filePart.data != null) {
                if (configuration.saveExcel()) setSaveFile(filePart);
                Mail.agent(configuration).sendFile(recipient, filePart.filename, filePart.data);
            }
        }
        httpExchange.sendResponseHeaders(200, 0);
        httpExchange.close();
    }

    private void setSaveFile(Part filePart) throws Exception {
        String fileName = filePart.filename != null && !filePart.filename.isEmpty() ? filePart.filename : "the_file";
        FileOutputStream outputStream = new FileOutputStream(configuration.excelDir() + fileName);
        outputStream.write(filePart.data);
        outputStream.flush();
        outputStream.close();
    }
}
