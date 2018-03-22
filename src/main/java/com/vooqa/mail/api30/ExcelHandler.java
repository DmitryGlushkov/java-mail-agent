package com.vooqa.mail.api30;

import com.sun.net.httpserver.HttpExchange;
import com.vooqa.mail.Configuration;
import com.vooqa.mail.Mail;
import com.vooqa.mail.http.FormDataMgr;
import com.vooqa.mail.http.Part;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.logging.Logger;

public class ExcelHandler extends BaseHandler {

    private final static Logger LOGGER = Logger.getLogger(ExcelHandler.class.getName());

    public ExcelHandler(Configuration configuration) {
        super(configuration);
    }

    @Override
    protected boolean doPost(HttpExchange httpExchange) throws Exception {
        final Map<String, Part> parts = FormDataMgr.handle(httpExchange);
        if (parts != null && parts.size() == 2) {
            String recipient = parts.containsKey("email") ? new String(parts.get("email").data) : null;
            LOGGER.info(String.format("recipient: %s", recipient));
            Part filePart = parts.get("file");
            if (filePart != null && filePart.data != null) {
                if (configuration.saveExcel()) setSaveFile(filePart);
                Mail.agent(configuration).sendFile(recipient, filePart.filename, filePart.data, filePart.type);
            } else {
                return bad(httpExchange, "Wrong excel file");
            }
        } else {
            String info = "";
            if(parts == null) {
                info += " Parts is NULL";
            } else {
                info += " email: "; info += parts.containsKey("email") ? new String(parts.get("email").data) : "NULL";
                info += " file: "; info += parts.containsKey("file") ? parts.get("file").filename : "NULL";
            }
            return bad(httpExchange, "Wrong form-data. " + info);
        }
        return true;
    }

    private boolean bad(HttpExchange httpExchange, String msg) throws Exception {
        final byte[] respBody = msg.getBytes();
        httpExchange.sendResponseHeaders(400, respBody.length);
        httpExchange.getResponseBody().write(respBody);
        httpExchange.close();
        return false;
    }

    private void setSaveFile(Part filePart) {
        try {
            String fileName = filePart.filename != null && !filePart.filename.isEmpty() ? filePart.filename : "the_file";
            FileOutputStream outputStream = new FileOutputStream(configuration.excelDir() + File.separator + fileName);
            outputStream.write(filePart.data);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
