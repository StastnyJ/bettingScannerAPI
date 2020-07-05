package com.bettingScanner.api.storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
// import java.util.concurrent.locks.ReadWriteLock;
// import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import com.bettingScanner.api.requests.Request;

public class LocalStorage implements Storage {
    private RequestsItem requests;
    private List<String> emails;

    public LocalStorage() {
        this.requests = new RequestsItem();
        this.emails = new ArrayList<>();
    }

    @Override
    public List<Request> getAllRequests() {
        return this.requests.getRequets();
    }

    @Override
    public List<Request> getWaitingRequests() {
        return this.requests.getRequets().stream().filter(req -> !req.isFinnished()).collect(Collectors.toList());
    }

    @Override
    public List<Request> getFinishedRequests() {
        return this.requests.getRequets().stream().filter(req -> req.isFinnished()).collect(Collectors.toList());
    }

    @Override
    public void finishRequest(Request req) {
        List<Request> actReqs = requests.getRequets();
        Optional<Request> act = actReqs.stream().filter(r -> r.equals(req)).findFirst();
        if (act.isPresent()) {
            act.get().finish();
            requests.setRequests(actReqs);
        }
    }

    @Override
    public void finishRequests(List<Request> reqs) {
        reqs.forEach(r -> finishRequest(r));
    }

    @Override
    public void addRequest(Request req) {
        requests.addRequest(req);
    }

    @Override
    public void addRequests(List<Request> reqs) {
        reqs.forEach(r -> requests.addRequest(r));
    }

    @Override
    public void deleteRequest(LocalDateTime createdTime) {
        requests.setRequests(requests.getRequets().stream().filter(r -> !r.getCreatedDate().equals(createdTime))
                .collect(Collectors.toList()));
    }

    @Override
    public void deleteRequests(List<Request> reqs) {
        requests.setRequests(
                requests.getRequets().stream().filter(r -> !reqs.contains(r)).collect(Collectors.toList()));
    }

    @Override
    public List<String> getEmails() {
        return this.emails.stream().collect(Collectors.toList());
    }

    @Override
    public void addEmail(String email) {
        if (emails.stream().filter(e -> e.equals(email)).count() == 0)
            this.emails.add(email);
    }

    @Override
    public void addEmails(List<String> emails) {
        if (emails == null)
            return;
        for (String email : emails)
            this.addEmail(email);
    }

    @Override
    public void removeEmail(String email) {
        this.emails = this.emails.stream().filter(e -> !e.equals(email)).collect(Collectors.toList());
    }

    @Override
    public void notifyUpdate() {

    }

    private class RequestsItem {
        private List<Request> requests;
        // private ReadWriteLock lock = new ReentrantReadWriteLock();

        public RequestsItem() {
            requests = new ArrayList<>();
        }

        // TODO - thread safe

        public List<Request> getRequets() {
            return new ArrayList<>(requests);
        }

        public void setRequests(List<Request> reqs) {
            requests = new ArrayList<>(reqs);
        }

        public void addRequest(Request req) {
            requests.add(req);
        }
    }
}