package com.bettingScanner.api.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.util.JSONPObject;

import org.apache.tomcat.util.buf.StringUtils;

public class WebScanningService {

    public static List<HttpCookie> getCookies(URL url) {
        HttpURLConnection con = null;
        List<HttpCookie> res = new ArrayList<>();
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                List<String> cookies = con.getHeaderFields().get("Set-Cookie");
                cookies.stream().map(HttpCookie::parse).forEach(sublist -> sublist.stream().forEach(s -> res.add(s)));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            if (con != null)
                con.disconnect();
        }
        return res;
    }

    public static String getJSessionId(URL url) {
        List<HttpCookie> cookies = getCookies(url);
        for (HttpCookie act : cookies) {
            if (act.getName().equals("JSESSIONID"))
                return act.getValue();
        }
        return "";
    }

    public static String getSiteContent(URL url) {
        HttpURLConnection con = null;
        StringBuffer res = new StringBuffer();
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    res.append(inputLine);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            if (con != null)
                con.disconnect();
        }
        return res.toString();
    }

    public static String getSiteContent(URL url, Map<String, String> jsonBodyParams, List<HttpCookie> cookies) {
        HttpURLConnection con = null;
        StringBuffer res = new StringBuffer();
        String bodyString = convertToJson(jsonBodyParams);
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            con.setRequestProperty("Cookie", StringUtils.join(
                    cookies.stream().map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.toList()), ';'));
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = bodyString.getBytes("utf-8");
                os.write(input, 0, input.length);
                os.flush();
            }
            int responseCode = con.getResponseCode();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    res.append(inputLine);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            if (con != null)
                con.disconnect();
        }
        return res.toString();
    }

    public static String getSiteContent(URL url, Map<String, String> jsonBodyParams, HttpCookie cookie) {
        List<HttpCookie> cookies = new ArrayList<>();
        cookies.add(cookie);
        return getSiteContent(url, jsonBodyParams, cookies);
    }

    private static String convertToJson(Map<String, String> params) {
        List<String> vals = new LinkedList<>();
        for (String key : params.keySet()) {
            vals.add(String.format("\"%s\":\"%s\"", key, params.get(key)));
        }
        return "{" + String.join(",", vals) + "}";
    }
}