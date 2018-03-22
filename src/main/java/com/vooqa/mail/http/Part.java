package com.vooqa.mail.http;

public class Part {

    public final String name;
    public final String filename;
    public final String type;
    public final byte[] data;

    public Part(String name, byte[] data, String filename, String type) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.filename = filename;
    }

}
