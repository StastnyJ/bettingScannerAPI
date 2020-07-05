package com.bettingScanner.api.storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bettingScanner.api.requests.Request;
import com.bettingScanner.api.services.StringSharerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CloudStorage implements Storage {
    private Storage storage;
    private static final String stringKey = "bettingScanner";

    public CloudStorage() {
        this.storage = new LocalStorage();
        try {
            fillStorage();
        } catch (JSONException ex) {
        }
    }

    @Override
    public List<Request> getAllRequests() {
        return storage.getAllRequests();
    }

    @Override
    public List<Request> getWaitingRequests() {
        return storage.getWaitingRequests();
    }

    @Override
    public List<Request> getFinishedRequests() {
        return storage.getFinishedRequests();
    }

    @Override
    public List<String> getEmails() {
        return storage.getEmails();
    }

    @Override
    public void finishRequest(Request req) {
        storage.finishRequest(req);
        saveToCloudAsync();
    }

    @Override
    public void finishRequests(List<Request> reqs) {
        storage.finishRequests(reqs);
        saveToCloudAsync();
    }

    @Override
    public void addRequest(Request req) {
        storage.addRequest(req);
        saveToCloudAsync();
    }

    @Override
    public void addRequests(List<Request> reqs) {
        storage.addRequests(reqs);
        saveToCloudAsync();
    }

    @Override
    public void deleteRequest(LocalDateTime createdTime) {
        storage.deleteRequest(createdTime);
        saveToCloudAsync();
    }

    @Override
    public void deleteRequests(List<Request> reqs) {
        storage.deleteRequests(reqs);
        saveToCloudAsync();
    }

    @Override
    public void addEmail(String email) {
        storage.addEmail(email);
        saveToCloudAsync();
    }

    @Override
    public void addEmails(List<String> emails) {
        storage.addEmails(emails);
        saveToCloudAsync();
    }

    @Override
    public void removeEmail(String email) {
        storage.removeEmail(email);
        saveToCloudAsync();
    }

    @Override
    public void notifyUpdate() {
        saveToCloud();
    }

    private void saveToCloudAsync() {
        Thread async = new Thread(() -> saveToCloud());
        async.start();
    }

    private synchronized void saveToCloud() {
        StringSharerService.postString(stringKey, serializeStorage());
    }

    private String serializeStorage() {
        StringBuilder res = new StringBuilder();
        res.append("{\"emails\": [");
        res.append(
                String.join(",", storage.getEmails().stream().map(e -> "\"" + e + "\"").collect(Collectors.toList())));
        res.append("],\"requests\":[");
        res.append(String.join(",",
                storage.getAllRequests().stream().map(r -> "\"" + r.toString() + "\"").collect(Collectors.toList())));
        res.append("]}");
        return res.toString();
    }

    private void fillStorage() throws JSONException {
        String rawData = StringSharerService.getString(stringKey);
        JSONObject json = new JSONObject(rawData);
        JSONArray emails = json.getJSONArray("emails");
        JSONArray data = json.getJSONArray("requests");
        List<Request> requests = new ArrayList<>();
        List<String> emailsList = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            requests.add(Request.parse(data.getString(i)));
        }
        for (int i = 0; i < emails.length(); i++) {
            emailsList.add(emails.getString(i));
        }

        storage.addRequests(requests);
        storage.addEmails(emailsList);
    }
}