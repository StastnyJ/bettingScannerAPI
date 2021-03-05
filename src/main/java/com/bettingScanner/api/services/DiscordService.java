package com.bettingScanner.api.services;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordService extends NotificationService {

    private final String webhookUrl;

    public DiscordService(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    protected boolean sendNotification(String text) {
        String message = formatMessage(text);

        HttpURLConnection con = null;
        try {
            URL url = new URL(webhookUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");

            con.setRequestProperty("Content-Type", "application/json");

            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = message.getBytes("utf-8");
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

    private String formatMessage(String text) {
        return ("{\"content\": \"" + text + "\"}").replaceAll("\\r|\\n", "");
    }
}
