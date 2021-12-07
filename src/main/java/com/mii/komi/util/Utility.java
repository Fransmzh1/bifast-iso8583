package com.mii.komi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class Utility {

    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String convertInputStreamToString(InputStream is) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(is, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return textBuilder.toString();
    }

    /* no longer used
    public static String getOriginalDateTimeFromOriginalNoRef(String originalNoRef) {
        int cursor = 0;
        int endCursor = cursor + 3;
        String prefix = originalNoRef.substring(cursor, endCursor);

        cursor = endCursor;
        endCursor = cursor + 2;
        String year = originalNoRef.substring(cursor, endCursor);

        cursor = endCursor;
        endCursor = cursor + 2;
        String month = originalNoRef.substring(cursor, endCursor);

        cursor = endCursor;
        endCursor = cursor + 2;
        String day = originalNoRef.substring(cursor, endCursor);

        cursor = endCursor;
        endCursor = cursor + 2;
        String hour = originalNoRef.substring(cursor, endCursor);

        cursor = endCursor;
        endCursor = cursor + 2;
        String minute = originalNoRef.substring(cursor, endCursor);

        cursor = endCursor;
        endCursor = cursor + 2;
        String second = originalNoRef.substring(cursor, endCursor);

        String originalDateTime = month + day + hour + minute + second;
        return originalDateTime;
    }
    */

    public static String getJSONMoney(String isomoney) {
        String sig = isomoney.substring(0, 16);
        String dec = isomoney.substring(16);
        return ISOUtil.zeroUnPad(sig) + "." + dec;
    }

    public static String getISOMoney(String jsonmoney) throws ISOException {
        // assume client will make sure input is valid number with format ################.00 (num(16,2))
        /* remove protection - invalid amount should generate exception
        if (jsonmoney.length() < 3) {
            return "000000000000000000"; // else returns zero
        }
        */
        String dec = jsonmoney.substring(jsonmoney.length() - 2);
        String sig = jsonmoney.substring(0, jsonmoney.length() - 3);
        return ISOUtil.zeropad(sig, 16) + dec;
    }

    public static String getISODateTime(String jsondatetime) {
        // input : yyyy-MM-ddTHH:mm:ss.SSS
        // output : MMddHHmmss
        int cursor = 0;
        int endCursor = cursor + 2;
        String mil = jsondatetime.substring(cursor, endCursor);

        cursor = endCursor;
        endCursor = cursor + 2;
        String year = jsondatetime.substring(cursor, endCursor);

        cursor = endCursor + 1;
        endCursor = cursor + 2;
        String month = jsondatetime.substring(cursor, endCursor);

        cursor = endCursor + 1;
        endCursor = cursor + 2;
        String date = jsondatetime.substring(cursor, endCursor);

        cursor = endCursor + 1;
        endCursor = cursor + 2;
        String hour = jsondatetime.substring(cursor, endCursor);

        cursor = endCursor + 1;
        endCursor = cursor + 2;
        String minute = jsondatetime.substring(cursor, endCursor);

        cursor = endCursor + 1;
        endCursor = cursor + 2;
        String second = jsondatetime.substring(cursor, endCursor);

        return month + date + hour + minute + second;
    }

    public static String getExceptionStackTraceAsString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
