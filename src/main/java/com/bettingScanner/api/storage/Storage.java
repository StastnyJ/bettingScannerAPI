package com.bettingScanner.api.storage;

import java.time.LocalDateTime;
import java.util.List;

import com.bettingScanner.api.requests.Request;

public interface Storage {
    public List<Request> getAllRequests();

    public List<Request> getFinishedRequests();

    public List<Request> getWaitingRequests();

    public void addRequest(Request req);

    public void deleteRequest(LocalDateTime createdTime);

    public void finishRequest(Request req);

    public String getEmail();

    public void setEmail(String email);

    public default void deleteRequest(Request req) {
        deleteRequest(req.getCreatedDate());
    }
}