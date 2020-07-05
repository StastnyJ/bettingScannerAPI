package com.bettingScanner.api.requests;

import java.time.LocalDateTime;

public class Request {
    protected final String scanUrl;
    protected final String displayUrl;
    protected final String keyword;
    protected final String email;
    protected boolean finnished;
    protected final LocalDateTime createdDate;

    public Request(String scanUrl, String displayUrl, String keyword, String email, LocalDateTime createDate,
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
        this.email = email;
        this.createdDate = createDate;
        this.finnished = isFinnished;
    }

    public Request(String scanUrl, String displayUrl, String keyword, String email) {
        this(scanUrl, displayUrl, keyword, email, null, false);
    }

    public Request(String scanUrl, String displayUrl, String keyword, String email, boolean isFinnished) {
        this(scanUrl, displayUrl, keyword, email, null, isFinnished);
    }

    public Request(String scanUrl, String keyword, String email, LocalDateTime createdDate) {
        this(scanUrl, scanUrl, keyword, email, createdDate, false);
    }

    public Request(String scanUrl, String keyword, String email, LocalDateTime createdDate, boolean isFinnished) {
        this(scanUrl, scanUrl, keyword, email, createdDate, isFinnished);
    }

    public Request(String scanUrl, String keyword, String email) {
        this(scanUrl, scanUrl, keyword, email);
    }

    public Request(String scanUrl, String keyword, String email, boolean isFinnished) {
        this(scanUrl, scanUrl, keyword, email, isFinnished);
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

    public String getEmail() {
        return email;
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
        return String.format("%s|%s|%s|%s|%s|%b", scanUrl, displayUrl, keyword, email, createdDate.toString(),
                finnished);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Request))
            return false;
        return this.createdDate.equals(((Request) obj).getCreatedDate());
    }
}