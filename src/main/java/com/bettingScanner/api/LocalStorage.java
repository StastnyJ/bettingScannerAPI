package com.bettingScanner.api;

import java.util.ArrayList;
import java.util.List;
// import java.util.concurrent.locks.ReadWriteLock;
// import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.bettingScanner.api.requests.Request;

public class LocalStorage {
    private RequestsItem waitingRequests;
    private RequestsItem finishedRequests;

    public LocalStorage() {
        this.waitingRequests = new RequestsItem();
        this.finishedRequests = new RequestsItem();
    }

    public List<Request> getWaitingRequests() {
        return waitingRequests.getRequets();
    }

    public List<Request> getFinishedRequests() {
        return finishedRequests.getRequets();
    }

    public void addWaitingRequest(Request req) {
        waitingRequests.addRequest(req);
    }

    public void addFinishedRequest(Request req) {
        finishedRequests.addRequest(req);
    }

    public void setWaitingRequests(List<Request> reqs) {
        waitingRequests.setRequests(reqs);
    }

    public void setFinishedRequests(List<Request> reqs) {
        finishedRequests.setRequests(reqs);
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