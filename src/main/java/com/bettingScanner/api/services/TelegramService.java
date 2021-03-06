package com.bettingScanner.api.services;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import reactor.util.function.Tuple2;

public class TelegramService extends NotificationService {
    private final static String botToken = "1368983189:AAHMvRxsZEgM3wAi0Zo-BRFPd9f_wD8JZG0";
    private final String chatId;

    public TelegramService(String chatId) {
        this.chatId = chatId;
    }

    @Override
    protected List<String> formatMessage(Stream<Tuple2<String, String>> records, String header) {
        StringBuilder body = new StringBuilder();
        body.append(header + ":\n\n");
        records.forEach(
                req -> body.append(String.format(" - *%s:* [%s](%s)\n", req.getT1(), req.getT2(), req.getT2())));
        List<String> res = new ArrayList<>();
        res.add(body.toString());
        return res;
    }

    protected boolean sendNotification(String text) {
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