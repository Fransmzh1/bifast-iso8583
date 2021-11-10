package com.mii.komi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.ParsePosition;
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

    public static String getJSONMoney(String isomoney) {
        String sig = isomoney.substring(0, 16);
        String dec = isomoney.substring(16);        
        return ISOUtil.zeroUnPad(sig) + "." + dec;
    }

    public static String getISOMoney(String jsonmoney) throws ISOException {
        // assume client will make sure input is valid number with format ################.00 (num(16,2))
        // just in-case, set to zero otherwise
        if (jsonmoney.length() < 3) return "000000000000000000"; // else returns zero

        String dec = jsonmoney.substring(jsonmoney.length() - 2);
        String sig = jsonmoney.substring(0, jsonmoney.length() - 3);
        return ISOUtil.zeropad(sig, 16) + dec;
    }
}
