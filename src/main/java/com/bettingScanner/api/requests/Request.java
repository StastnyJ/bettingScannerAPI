package com.bettingScanner.api.requests;

import java.time.LocalDateTime;

public class Request {
    private final String scanUrl;
    private final String displayUrl;
    private final String keyword;
    private final LocalDateTime createdDate;

    public Request(String scanUrl, String displayUrl, String keyword, LocalDateTime createDate) {
        if (scanUrl == null || scanUrl == "")
            throw new NullPointerException("Scan url can not be null or empty");
        if (displayUrl == null || displayUrl == "")
            displayUrl = scanUrl;
        if (keyword == null || keyword == "")
            throw new NullPointerException("Keyword can not be null or empty");
        if (createDate == null)
            createDate = LocalDateTime.now();
        this.scanUrl = scanUrl;
        this.displayUrl = displayUrl;
        this.keyword = keyword;
        this.createdDate = createDate;
    }

    public Request(String scanUrl, String displayUrl, String keyword) {
        this(scanUrl, displayUrl, keyword, null);
    }

    public Request(String scanUrl, String keyword, LocalDateTime createdDate) {
        this(scanUrl, scanUrl, keyword, createdDate);
    }

    public Request(String scanUrl, String keyword) {
        this(scanUrl, scanUrl, keyword);
    }

    public static Request parse(String pattern) {
        if (pattern == null || pattern.length() == 0)
            throw new NullPointerException("Pattern can not be null");
        String[] data = pattern.split("\\|");
        if (data.length != 4) {
            throw new IllegalArgumentException("Pattern is in incorrect format");
        }
        return new Request(data[0], data[1], data[2], LocalDateTime.parse(data[3]));
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

    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s", scanUrl, displayUrl, keyword, createdDate.toString());
    }
}