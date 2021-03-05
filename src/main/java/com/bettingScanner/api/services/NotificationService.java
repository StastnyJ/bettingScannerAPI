package com.bettingScanner.api.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.bettingScanner.api.notifications.ChatInfo;
import com.bettingScanner.api.notifications.ChatsRepository;
import com.bettingScanner.api.requests.Request;
import com.bettingScanner.api.tipsport.Match;

public abstract class NotificationService {

    protected abstract boolean sendNotification(String text);

    public static NotificationService getService(String chatId, ChatsRepository repo) {
        ChatInfo chat = repo.findById(chatId).orElse(null);
        if (chat == null)
            throw new NoSuchElementException();
        return NotificationService.getService(chat);
    }

    public static NotificationService getService(ChatInfo chat) {
        if (chat.getPlatform().equals(ChatInfo.Platforms.TELEGRAM))
            return new TelegramService(chat.getChatId());
        else
            return new DiscordService(chat.getDetails());
    }

    public static void notifyFounds(List<Request> reqs, ChatsRepository repo) {
        Map<String, List<Request>> groups = new HashMap<>();
        for (Request req : reqs) {
            if (!groups.containsKey(req.getChatId()))
                groups.put(req.getChatId(), new ArrayList<>());
            groups.get(req.getChatId()).add(req);
        }
        for (String chatId : groups.keySet()) {
            NotificationService.notifyFounds(groups.get(chatId), chatId, repo);
        }
    }

    private static void notifyFounds(List<Request> reqs, String chatId, ChatsRepository repo) {
        StringBuilder body = new StringBuilder();
        body.append("Scanning service found one or more keywords on the following websites:\n\n");
        reqs.stream().forEach(req -> body.append(
                String.format("\t- *%s:* [%s](%s)\n", req.getKeyword(), req.getDisplayUrl(), req.getDisplayUrl())));
        NotificationService.getService(chatId, repo).sendNotification(body.toString());
    }

    public static void notifyStateChange(List<Match> matches, String chatId, ChatsRepository repo) {
        StringBuilder body = new StringBuilder();
        body.append("Scanning service found new matches on the following websites:\n\n");
        matches.stream().forEach(m -> body
                .append(String.format("\t- *%s:* [%s](%s)\n", m.getDescription(), m.getMatchUrl(), m.getMatchUrl())));
        NotificationService.getService(chatId, repo).sendNotification(body.toString());
    }

    public static void testNotification(String chatId, ChatsRepository repo) {
        NotificationService.getService(chatId, repo).sendNotification("*This is test notification*");
    }
}
