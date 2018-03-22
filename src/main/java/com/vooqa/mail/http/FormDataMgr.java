package com.vooqa.mail.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.InputStream;
import java.util.*;

public class FormDataMgr {

    private final static String BOUNDARY_KEY = "boundary=";
    private final static String BOUNDARY_PREFIX = "--";
    private final static String CRLF = "\r\n";

    public static List<Part> handle(final HttpExchange httpExchange) throws Exception {

        final String contentType = httpExchange.getRequestHeaders().getFirst("Content-type");
        if (contentType.contains("multipart/form-data")) {
            final List<Part> partsList = new ArrayList<>();
            final String boundaryKey = contentType.substring(contentType.indexOf(BOUNDARY_KEY) + BOUNDARY_KEY.length());
            final byte[] boundary = (BOUNDARY_PREFIX + boundaryKey).getBytes();
            final byte[] content = getContentBytes(httpExchange);
            final List<byte[]> parts = splitByteArray(content, boundary);
            // get headers of every part
            byte[] headerDelimiter = new byte[]{13, 10, 13, 10};    // \r\n\r\n
            for (byte[] _b : parts) {
                int position = indexOf(_b, headerDelimiter);
                Map<String, String> params = getHeaderParameters(new String(Arrays.copyOfRange(_b, 0, position)));
                byte[] partData = Arrays.copyOfRange(_b, position + 4, _b.length);  // -2: CRLF in the end of data
                partsList.add(new Part(params.get("name"), partData));
            }
            return partsList;
        }
        return null;
    }

    private static Map<String, String> getHeaderParameters(String headers) {
        final Map<String, String> result = new HashMap<>();
        String[] lines = headers.split(CRLF);
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("Content-Disposition")) {
                String[] params = line.split(";");
                for (String p : params){
                    String[] vals = p.split("=");
                    if(vals.length == 2) {
                        result.put(vals[0].trim(), vals[1].trim().replaceAll("\"", ""));
                        System.out.println();
                    }
                }
            }
        }
        return result;
    }

    private static int indexOf(byte[] source, byte[] entry) {
        int _p = 0;
        int entryStart = 0;
        for (int i = 0; i < source.length; i++) {
            byte b = source[i];
            if (_p < entry.length && entry[_p] == b) {
                if (_p == 0) entryStart = i;
                else if (_p == entry.length - 1) return entryStart;
                _p++;
            } else {
                _p = 0;
            }
        }
        return -1;
    }

    // split 'source' to parts using 'entry' as s delimiter
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
                        dataEnd = entryStart;
                        split.add(Arrays.copyOfRange(source, dataStart+2, dataEnd-2));  // 2 is CRLF
                    }
                    dataStart = i;
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
        final byte[] buffer = new byte[2048];
        byte b;
        int p = 0;
        int count = 0;
        while (count != -1) {
            count = is.read(buffer);
            for (int i = 0; i < count; i++) {
                result[p++] = buffer[i];
            }
        }
        return result;
    }

}
