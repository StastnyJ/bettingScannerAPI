package com.bettingScanner.api.notifications;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "Chats")
@Entity
public class ChatInfo {
    public static class Platforms {
        public static final String TELEGRAM = "Telegram";
        public static final String DISCORD = "Discord";
    }

    @Id
    private String chatId;
    private String name;
    private Boolean visible;
    private String platform;
    private String details;

    public ChatInfo() {
    }

    public ChatInfo(String chatId, String name, String platform) {
        this(chatId, name, platform, "");
    }

    public ChatInfo(String chatId, String name, String platform, String details) {
        this.chatId = chatId;
        this.name = name;
        this.platform = platform;
        this.visible = true;
        this.details = details;
    }

    public String getChatId() {
        return this.chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getVisible() {
        return this.visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return name + " (" + chatId + ")";
    }
}