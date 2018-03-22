package com.vooqa.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Configuration {

    private static Properties prop = new Properties();

    public Configuration(File file) throws Exception {
        prop.load(new InputStreamReader(new FileInputStream(file),"UTF-8"));
    }

    public String adminMailBox() {
        return prop.getProperty("adminMailBox");
    }

    public Integer serverPort() {
        return Integer.parseInt(prop.getProperty("serverPort"));
    }

    public String smtpHost() {
        return prop.getProperty("smtpHost");
    }

    public Integer smtpPort() {
        return Integer.parseInt(prop.getProperty("smtpPort"));
    }

    public String mailBox() {
        return prop.getProperty("mailBox");
    }

    public String mailBoxPassword() {
        return prop.getProperty("mailBoxPassword");
    }

    public String excelDir() {
        return prop.getProperty("excelDir");
    }

    public Boolean saveExcel() {
        return Boolean.parseBoolean(prop.getProperty("saveExcel"));
    }

    public String mailListFile() {
        return prop.getProperty("mailListFile");
    }
}
