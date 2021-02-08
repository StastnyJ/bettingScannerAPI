package com.bettingScanner.api.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bettingScanner.api.requests.Request;
import com.bettingScanner.api.tipsport.Match;

import org.apache.tomcat.util.buf.StringUtils;

public class WebScanningService {
    public static String tipsportJSessionId = "";

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
        return getSiteContent(url, new ArrayList<>(), null);
    }

    public static String getSiteContent(URL url, HttpCookie cookie) {
        List<HttpCookie> cookies = new ArrayList<>();
        cookies.add(cookie);
        return getSiteContent(url, cookies, null);
    }

    public static String getSiteContent(URL url, List<HttpCookie> cookies, String authorization) {
        // HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection con = null;
        StringBuffer res = new StringBuffer();
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            if (cookies != null && cookies.size() > 0)
                con.setRequestProperty("Cookie", StringUtils.join(
                        cookies.stream().map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.toList()), ';'));
            if (authorization != null && authorization.length() > 0)
                con.setRequestProperty("Authorization", authorization);
            int responseCode = con.getResponseCode();
            if (responseCode >= 400 && responseCode < 500)
                return "unauthorized";
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
        // HttpURLConnection.setFollowRedirects(false);
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
            if (responseCode >= 400 && responseCode < 500)
                return "unauthorized";
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

    private static boolean scanRequest(Request req, boolean repeated) throws MalformedURLException {
        if (tipsportJSessionId.length() == 0)
            tipsportJSessionId = getJSessionId(new URL("https://www.tipsport.cz/"));
        String siteContent;
        if (req.getScanUrl().contains("betting-scanner-api"))
            siteContent = getSiteContent(new URL(req.getScanUrl()), null, "Basic YmV0bHVraTpxbXlwZmdoMTc=");
        else
            siteContent = getSiteContent(new URL(req.getScanUrl()), new HttpCookie("JSESSIONID", tipsportJSessionId));
        if (siteContent.equals("unauthorized") && !repeated) {
            tipsportJSessionId = "";
            return scanRequest(req, true);
        }
        return siteContent.toLowerCase().contains(req.getKeyword().toLowerCase());
    }

    public static List<Match> scanStateRequest(Request req) {
        String url = req.getScanUrl().replace("--", "-&");
        Integer id = Integer.parseInt((url.split("-")[url.split("-").length - 1]).replace("&", "-"));
        Map<String, String> params = new HashMap<>();
        params.put("url", req.getScanUrl());
        params.put("id", id.toString());
        params.put("results", "false");
        params.put("type", req.getTipsportCategory());
        try {
            String sessionId = WebScanningService.getJSessionId(new URL("https://www.tipsport.cz/"));
            String res = WebScanningService.getSiteContent(
                    new URL("https://m.tipsport.cz/rest/offer/v1/offer?limit=200"), params,
                    new HttpCookie("JSESSIONID", sessionId));
            List<Match> actMatches = Match.parseFromJson(res);
            List<Match> oldMatches;
            if (req.getState().length() > 0)
                oldMatches = Arrays.stream(req.getState().split(";")).map(m -> Match.parse(m))
                        .collect(Collectors.toList());
            else
                oldMatches = new ArrayList<>();
            List<Match> newMatches = actMatches.stream().filter(m -> !oldMatches.contains(m))
                    .collect(Collectors.toList());
            actMatches.addAll(oldMatches.stream().filter(m -> !actMatches.contains(m)).collect(Collectors.toList()));
            req.setState(String.join(";", actMatches.stream().map(m -> m.toString()).collect(Collectors.toList())));
            return newMatches;
        } catch (MalformedURLException ex) {
            return new ArrayList<>();
        }
    }

    public static boolean scanRequest(Request req) throws MalformedURLException {
        return scanRequest(req, false);
    }
}