package com.bettingScanner.api.services;

import java.util.List;

import com.bettingScanner.api.requests.Request;
import com.bettingScanner.api.tipsport.Match;

public class TelegramService {
    // private final static String botToken =
    // "1368983189:AAHMvRxsZEgM3wAi0Zo-BRFPd9f_wD8JZG0";

    public static void notifyFounds(List<Request> reqs) {
        // Map<String, List<Request>> groups = new HashMap<>();
        // for (Request req : reqs) {
        // if (!groups.containsKey(req.getEmail()))
        // groups.put(req.getEmail(), new ArrayList<>());
        // groups.get(req.getEmail()).add(req);
        // }
        // for (String email : groups.keySet()) {
        // notifyFounds(groups.get(email), email);
        // }
    }

    public static void notifyFounds(List<Request> reqs, String chatId) {
        // StringBuilder body = new StringBuilder();
        // body.append("<p>Scanning service found one or more keywords on the following
        // websites:</p><br/><ul>");
        // reqs.stream().forEach(req -> body.append(String.format("<li><b>%s:</b> <a
        // href=\"%s\">%s</a></li>",
        // req.getKeyword(), req.getDisplayUrl(), req.getDisplayUrl())));
        // body.append("</ul>");
        // sendEmail(email, "Scanning service found one or more keywords",
        // body.toString());
    }

    public static void testNotification(String chatId) {
        // sendEmail(email, "Test email", "<b>This is test email</b>");
    }

    public static void notifyStateChange(List<Match> matches, String email) {
        // StringBuilder body = new StringBuilder();
        // body.append("<p>Scanning service found new matches on the following
        // websites:</p><br/><ul>");
        // matches.stream().forEach(m -> body.append(String.format("<li><b>%s:</b> <a
        // href=\"%s\">%s</a></li>",
        // m.getDescription(), m.getMatchUrl(), m.getMatchUrl())));
        // body.append("</ul>");
        // sendEmail(email, "Scanning service found new matches", body.toString());
    }
}