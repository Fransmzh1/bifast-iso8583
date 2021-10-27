package com.mii.komi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

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

}
