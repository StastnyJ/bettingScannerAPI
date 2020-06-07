package com.bettingScanner.api.requests;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bettingScanner.api.BettingScannerApiApplication;
import com.bettingScanner.api.services.EmailingService;
import com.bettingScanner.api.services.WebScanningService;
import com.bettingScanner.api.storage.Storage;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/requests/v1")
public class RequestsController {

    final Storage storage = BettingScannerApiApplication.localStorage;

    @GetMapping("/all")
    public List<Request> getAllRequests() {
        return storage.getAllRequests().stream().sorted((a, b) -> {
            if (!a.isFinnished() && b.isFinnished())
                return -1;
            if (a.isFinnished() && !b.isFinnished())
                return 1;
            return 0;
        }).collect(Collectors.toList());
    }

    @GetMapping("/waiting")
    public List<Request> getWaitingRequests() {
        return storage.getWaitingRequests();
    }

    @GetMapping("/finished")
    public List<Request> getFinishedRequests() {
        return storage.getFinishedRequests();
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public void deleteRequest(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {

        storage.deleteRequest(date);
    }

    @PostMapping(value = "/")
    public Request postMethodName(@RequestParam String url, @RequestParam String keyword,
            @RequestParam(defaultValue = "") String matchUrl) {

        Request newRequest = new Request(url, matchUrl, keyword);
        storage.addRequest(newRequest);
        return newRequest;
    }

    @PostMapping(value = "/scan")
    public List<Request> scan() {
        List<Request> requests = storage.getWaitingRequests();
        List<Request> result = new ArrayList<>();
        for (Request act : requests) {
            try {
                if (WebScanningService.scanRequest(act)) {
                    result.add(act);
                }
            } catch (MalformedURLException ex) {
                storage.deleteRequest(act);
            }
        }
        if (result.size() > 0) {
            storage.finishRequests(result);
            EmailingService.NotifyFounds(result, storage.getEmail());
        }
        return result;
    }
}