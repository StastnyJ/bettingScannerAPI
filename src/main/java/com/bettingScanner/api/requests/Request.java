package com.bettingScanner.api.requests;

import java.time.LocalDateTime;

public class Request {
    protected final String scanUrl;
    protected final String displayUrl;
    protected final String keyword;
    protected final String chatId;
    protected boolean finnished;
    protected final LocalDateTime createdDate;

    public Request(String scanUrl, String displayUrl, String keyword, String chatId, LocalDateTime createDate,
            boolean isFinnished) {
        if (scanUrl == null || scanUrl.length() == 0)
            throw new NullPointerException("Scan url can not be null or empty");
        if (displayUrl == null || displayUrl.length() == 0)
            displayUrl = scanUrl;
        if (createDate == null)
            createDate = LocalDateTime.now();
        this.scanUrl = scanUrl;
        this.displayUrl = displayUrl;
        this.keyword = keyword;
        this.chatId = chatId;
        this.createdDate = createDate;
        this.finnished = isFinnished;
    }

    public Request(String scanUrl, String displayUrl, String keyword, String chatId) {
        this(scanUrl, displayUrl, keyword, chatId, null, false);
    }

    public Request(String scanUrl, String displayUrl, String keyword, String chatId, boolean isFinnished) {
        this(scanUrl, displayUrl, keyword, chatId, null, isFinnished);
    }

    public Request(String scanUrl, String keyword, String chatId, LocalDateTime createdDate) {
        this(scanUrl, scanUrl, keyword, chatId, createdDate, false);
    }

    public Request(String scanUrl, String keyword, String chatId, LocalDateTime createdDate, boolean isFinnished) {
        this(scanUrl, scanUrl, keyword, chatId, createdDate, isFinnished);
    }

    public Request(String scanUrl, String keyword, String chatId) {
        this(scanUrl, scanUrl, keyword, chatId);
    }

    public Request(String scanUrl, String keyword, String chatId, boolean isFinnished) {
        this(scanUrl, scanUrl, keyword, chatId, isFinnished);
    }

    public String getScanUrl() {
        return scanUrl;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public String getKeyword() {
        return keyword;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getChatId() {
        return chatId;
    }

    public boolean isFinnished() {
        return finnished;
    }

    public void finish() {
        this.finnished = true;
    }

    public boolean hasState() {
        return false;
    }

    public static Request parse(String raw) {
        String[] data = raw.split("\\|");
        if (data.length == 6)
            return new Request(data[0], data[1], data[2], data[3], LocalDateTime.parse(data[4]),
                    Boolean.parseBoolean(data[5]));
        else if (data.length == 7)
            return StateRequest.parse(raw);
        return Request.empty();
    }

    public static Request empty() {
        return new Request("", "", "");
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s|%s|%b", scanUrl, displayUrl, keyword, chatId, createdDate.toString(),
                finnished);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Request))
            return false;
        return this.createdDate.equals(((Request) obj).getCreatedDate());
    }
}