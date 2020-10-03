package com.bettingScanner.api.services;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class StringSharerService {
    private final static String address = "https://erecept.lekarnaselska.cz/stringSharer/api.php";

    public static String getString(String key) {
        try {
            return WebScanningService.getSiteContent(new URL(address + "?key=" + key));
        } catch (MalformedURLException ex) {
            return "";
        }
    }

    public static boolean postString(String key, String value) {
        HttpURLConnection con = null;
        int responseCode = 0;
        try {
            con = (HttpURLConnection) new URL(address + "?key=" + key).openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = value.getBytes("utf-8");
                os.write(input, 0, input.length);
                os.flush();
            }
            responseCode = con.getResponseCode();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        if (con != null)
            con.disconnect();
        return responseCode == 200;
    }
}