package com.vooqa.mail;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Configuration file needed.");
            System.exit(1);
        } else {
            final File configFile = new File(args[0]);
            if (!configFile.exists()) {
                System.out.println("Wrong configuration file.");
                System.exit(1);
            } else {
                final Configuration conf = new Configuration(configFile);
                new Server().start(conf);
            }
        }

    }

}
