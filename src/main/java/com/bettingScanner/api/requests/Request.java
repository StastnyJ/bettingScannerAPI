package com.bettingScanner.api.requests;

import java.time.LocalDateTime;

public class Request {
    private final String scanUrl;
    private final String displayUrl;
    private final String keyword;
    private boolean finnished;
    private final LocalDateTime createdDate;

    public Request(String scanUrl, String displayUrl, String keyword, LocalDateTime createDate, boolean isFinnished) {
        if (scanUrl == null || scanUrl.length() == 0)
            throw new NullPointerException("Scan url can not be null or empty");
        if (displayUrl == null || displayUrl.length() == 0)
            displayUrl = scanUrl;
        if (keyword == null || keyword.length() == 0)
            throw new NullPointerException("Keyword can not be null or empty");
        if (createDate == null)
            createDate = LocalDateTime.now();
        this.scanUrl = scanUrl;
        this.displayUrl = displayUrl;
        this.keyword = keyword;
        this.createdDate = createDate;
        this.finnished = isFinnished;
    }

    public Request(String scanUrl, String displayUrl, String keyword) {
        this(scanUrl, displayUrl, keyword, null, false);
    }

    public Request(String scanUrl, String displayUrl, String keyword, boolean isFinnished) {
        this(scanUrl, displayUrl, keyword, null, isFinnished);
    }

    public Request(String scanUrl, String keyword, LocalDateTime createdDate) {
        this(scanUrl, scanUrl, keyword, createdDate, false);
    }

    public Request(String scanUrl, String keyword, LocalDateTime createdDate, boolean isFinnished) {
        this(scanUrl, scanUrl, keyword, createdDate, isFinnished);
    }

    public Request(String scanUrl, String keyword) {
        this(scanUrl, scanUrl, keyword);
    }

    public Request(String scanUrl, String keyword, boolean isFinnished) {
        this(scanUrl, scanUrl, keyword, isFinnished);
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

    public boolean isFinnished() {
        return finnished;
    }

    public void finish() {
        this.finnished = true;
    }

    public static Request parse(String raw) {
        String[] data = raw.split("\\|");
        if (data.length != 5)
            return Request.empty();
        return new Request(data[0], data[1], data[2], LocalDateTime.parse(data[3]), Boolean.parseBoolean(data[4]));
    }

    public static Request empty() {
        return new Request("", "", "");
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s|%b", scanUrl, displayUrl, keyword, createdDate.toString(), finnished);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Request))
            return false;
        return this.createdDate.equals(((Request) obj).getCreatedDate());
    }
}