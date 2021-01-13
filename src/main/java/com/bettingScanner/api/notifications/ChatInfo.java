package com.bettingScanner.api.notifications;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "Chats")
@Entity
public class ChatInfo {
    @Id
    private String chatId;
    private String name;
    private Boolean visible;

    public ChatInfo() {
    }

    public ChatInfo(String chatId, String name) {
        this.chatId = chatId;
        this.name = name;
        this.visible = true;
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

    @Override
    public String toString() {
        return name + " (" + chatId + ")";
    }
}