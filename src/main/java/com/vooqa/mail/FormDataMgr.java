package com.vooqa.mail;

import com.sun.net.httpserver.HttpExchange;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormDataMgr {

    private final static String BOUNDARY_KEY = "boundary=";
    private final static String BOUNDARY_PREFIX = "--";

    public static void handle(final HttpExchange httpExchange) throws Exception {
        final String contentType = httpExchange.getRequestHeaders().getFirst("Content-type");
        if (contentType.contains("multipart/form-data")) {
            final String boundaryKey = contentType.substring(contentType.indexOf(BOUNDARY_KEY) + BOUNDARY_KEY.length());
            final byte[] boundary = (BOUNDARY_PREFIX + boundaryKey).getBytes();
            final byte[] content = getContentBytes(httpExchange);
            final List<byte[]> parts = splitByteArray(content, boundary);
            System.out.println("-------------------------");
            for (byte[] _b : parts) {
                System.out.println(new String(_b));
            }
        }
    }


    private static List<byte[]> splitByteArray(byte[] source, byte[] entry) {
        final List<byte[]> split = new ArrayList<>();
        int _p = 0;
        int entryStart = 0;
        int dataStart = -1;
        int dataEnd;
        boolean bound = false;
        for (int i = 0; i < source.length; i++) {
            byte b = source[i];
            if (_p < entry.length && entry[_p] == b) {
                if (_p == 0) entryStart = i;
                _p++;
                bound = true;
            } else {
                if (bound && _p == entry.length) {
                    if (dataStart > -1) {
                        dataEnd = entryStart - 1;
                        split.add(Arrays.copyOfRange(source, dataStart, dataEnd));
                    }
                    dataStart = i + 1;
                }
                bound = false;
                _p = 0;
            }
        }
        return split;
    }

    private static byte[] getContentBytes(final HttpExchange httpExchange) throws Exception {
        final int len = Integer.parseInt(httpExchange.getRequestHeaders().getFirst("Content-length"));
        final byte[] result = new byte[len];
        final InputStream is = httpExchange.getRequestBody();
        byte b;
        int p = 0;
        while ((b = (byte) is.read()) != -1) result[p++] = b;
        return result;
    }
}
