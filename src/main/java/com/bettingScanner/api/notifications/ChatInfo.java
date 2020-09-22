package com.bettingScanner.api.notifications;

public class ChatInfo {
    private final String chatId;
    private final String userName;

    public ChatInfo(String chatId, String userName) {
        this.chatId = chatId;
        this.userName = userName;
    }

    public String getChatId() {
        return chatId;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return this.userName + " (" + this.chatId + ")";
    }

    public static ChatInfo parseChat(String raw) {
        int idStart = raw.lastIndexOf("(") + 1;
        String id = raw.substring(idStart, raw.length() - 1);
        String name = raw.substring(0, idStart - 2);
        return new ChatInfo(id, name);
    }
}
