package com.bettingScanner.api.storage;

import java.time.LocalDateTime;
import java.util.List;

import com.bettingScanner.api.requests.Request;

public interface Storage {
    public List<Request> getAllRequests();

    public List<Request> getFinishedRequests();

    public List<Request> getWaitingRequests();

    public void addRequest(Request req);

    public void addRequests(List<Request> reqs);

    public void deleteRequest(LocalDateTime createdTime);

    public void deleteRequests(List<Request> createdTimes);

    public void finishRequest(Request req);

    public void finishRequests(List<Request> reqs);

    public List<String> getEmails();

    public void addEmail(String email);

    public void addEmails(List<String> emails);

    public void removeEmail(String email);

    public default void deleteRequest(Request req) {
        deleteRequest(req.getCreatedDate());
    }
}