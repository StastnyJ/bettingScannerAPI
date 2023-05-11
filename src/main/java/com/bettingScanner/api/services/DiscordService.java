package com.bettingScanner.api.services;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.util.function.Tuple2;

public class DiscordService extends NotificationService {

    private final String webhookUrl;

    public DiscordService(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    @Override
    protected List<String> formatMessage(Stream<Tuple2<String, String>> records, String header) {
        final String template = "{\"content\": \"%s\", \"embeds\": [{\"fields\": [%s]}]} ";
        final String fieldTemplate = "{\"name\": \"%s\",\"value\": \"%s\"}";

        List<String> res = new ArrayList<>();
        List<Tuple2<String, String>> recordsList = records.collect(Collectors.toList());

        for (int i = 0; i * 25 < recordsList.size(); i++) {
            List<String> act = recordsList.subList(25 * i, Math.min(25 * (i + 1), recordsList.size())).stream()
                    .map(r -> String.format(fieldTemplate, r.getT1(), r.getT2())).collect(Collectors.toList());
            res.add(String.format(template, header, String.join(",", act)));
        }

        return res;
    }

    protected boolean sendNotification(String text) {
        HttpURLConnection con = null;
        try {
            URL url = new URL(webhookUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");

            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("user-agent", "curl/7.81.0");

            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = text.getBytes("utf-8");
                os.write(input, 0, input.length);
                os.flush();
            }
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
