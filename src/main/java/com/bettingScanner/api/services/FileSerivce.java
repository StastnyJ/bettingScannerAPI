package com.bettingScanner.api.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.bettingScanner.api.requests.Request;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class FileSerivce {
    private static final String finnishedFile = "static/FinishedScans.csv";
    private static final String waitingScans = "static/WaitingScans.csv";

    public static List<Request> loadWaitingRequests() {
        return loadRequests(waitingScans);
    }

    public static List<Request> loadFinishedRequests() {
        return loadRequests(finnishedFile);
    }

    public static void saveFinishedRequests(List<Request> requests) {
        saveRequests(finnishedFile, requests);
    }

    public static void saveWaitingRequests(List<Request> requests) {
        saveRequests(waitingScans, requests);
    }

    public static void addFinishedRequest(Request req) {
        addRequest(finnishedFile, req);
    }

    public static void addWaitingRequest(Request req) {
        addRequest(waitingScans, req);
    }

    private static void addRequest(String fileName, Request req) {
        Resource resource = new ClassPathResource(fileName);
        try {
            File file = resource.getFile();
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(req.toString() + "\n");
            }
        } catch (IOException ex) {
            ex.getStackTrace();
        }
    }

    private static void saveRequests(String fileName, List<Request> requests) {
        Resource resource = new ClassPathResource(fileName);
        String content = String.join("\n", requests.stream().map(Request::toString).collect(Collectors.toList()));
        try {
            File file = resource.getFile();
            try (FileWriter writer = new FileWriter(file, false)) {
                writer.write(content);
            }
        } catch (IOException ex) {
            ex.getStackTrace();
        }
    }

    private static List<Request> loadRequests(String fileName) {
        List<Request> res = new ArrayList<>();
        Resource resource = new ClassPathResource(fileName);
        try {
            File file = resource.getFile();
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String row = scanner.nextLine();
                    if (row.length() == 0)
                        continue;
                    res.add(Request.parse(row));
                }
            }
        } catch (IOException ex) {
            ex.getStackTrace();
        }
        return res;
    }
}