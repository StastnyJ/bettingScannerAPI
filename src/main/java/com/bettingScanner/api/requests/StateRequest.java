package com.bettingScanner.api.requests;

import java.time.LocalDateTime;

public class StateRequest extends Request {

    private String lastState;

    public StateRequest(String scanUrl, String displayUrl, String keyword, String email, LocalDateTime createDate,
            String lastState, boolean isFinnished) {
        super(scanUrl, displayUrl, keyword, email, createDate, isFinnished);
        this.lastState = lastState;
    }

    public StateRequest(String scanUrl, String displayUrl, String email) {
        this(scanUrl, displayUrl, "Watching status", email, LocalDateTime.now(), "", false);
    }

    public String getLastState() {
        return this.lastState;
    }

    public void changeState(String newState) {
        this.lastState = newState;
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s|%s|%s|%b", scanUrl, displayUrl, keyword, email, createdDate.toString(),
                lastState, finnished);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Request))
            return false;
        return this.createdDate.equals(((Request) obj).getCreatedDate());
    }

    @Override
    public boolean hasState() {
        return true;
    }

    public static StateRequest empty() {
        return new StateRequest("", "", "", "", LocalDateTime.now(), "", true);
    }

    public static StateRequest parse(String raw) {
        String[] data = raw.split("\\|");
        if (data.length != 7)
            return StateRequest.empty();
        return new StateRequest(data[0], data[1], data[2], data[3], LocalDateTime.parse(data[4]), data[5],
                Boolean.parseBoolean(data[6]));
    }
}