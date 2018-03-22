package com.vooqa.mail.http;

public class Part {

    public final String name;
    public final String filename;
    public final byte[] data;

    public Part(String name, byte[] data, String filename) {
        this.name = name;
        this.data = data;
        this.filename = filename;
    }

}
