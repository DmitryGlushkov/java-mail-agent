package com.vooqa.mail.http;

public class Part {

    public final String name;
    public final byte[] data;
    public Part(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }

}
