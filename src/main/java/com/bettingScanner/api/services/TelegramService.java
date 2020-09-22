package com.bettingScanner.api.services;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bettingScanner.api.requests.Request;
import com.bettingScanner.api.tipsport.Match;

public class TelegramService {
    private final static String botToken = "1368983189:AAHMvRxsZEgM3wAi0Zo-BRFPd9f_wD8JZG0";

    public static void notifyFounds(List<Request> reqs) {
        Map<String, List<Request>> groups = new HashMap<>();
        for (Request req : reqs) {
            if (!groups.containsKey(req.getChatId()))
                groups.put(req.getChatId(), new ArrayList<>());
            groups.get(req.getChatId()).add(req);
        }
        for (String email : groups.keySet()) {
            notifyFounds(groups.get(email), email);
        }
    }

    public static void notifyFounds(List<Request> reqs, String chatId) {
        StringBuilder body = new StringBuilder();
        body.append("Scanning service found one or more keywords on the following websites:\n\n");
        reqs.stream().forEach(req -> body.append(
                String.format("\t- *%s:* [%s](%s)\n", req.getKeyword(), req.getDisplayUrl(), req.getDisplayUrl())));
        sendNotification(chatId, body.toString());
    }

    public static void testNotification(String chatId) {
        TelegramService.sendNotification(chatId, "*This is test notification*");
    }

    public static void notifyStateChange(List<Match> matches, String chatId) {
        StringBuilder body = new StringBuilder();
        body.append("Scanning service found new matches on the following websites:\n\n");
        matches.stream().forEach(m -> body
                .append(String.format("\t- *%s:* [%s](%s)\n", m.getDescription(), m.getMatchUrl(), m.getMatchUrl())));
        sendNotification(chatId, body.toString());
    }

    private static boolean sendNotification(String chatId, String text) {
        HttpURLConnection con = null;
        try {
            URL url = new URL("https://api.telegram.org/bot" + botToken + "/sendMessage?chat_id=" + chatId + "&text="
                    + URLEncoder.encode(text, StandardCharsets.UTF_8.toString()) + "&parse_mode=markdown");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode >= 400 && responseCode < 500)
                return false;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            if (con != null)
                con.disconnect();
        }
        return true;
    }
}