package com.bettingScanner.api.requests;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.bettingScanner.api.BettingScannerApiApplication;
import com.bettingScanner.api.LocalStorage;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/requests/v1")
public class RequestsController {

    final LocalStorage storage = BettingScannerApiApplication.localStorage;

    @GetMapping("/all")
    public List<Request> getAllRequests() {
        return storage.getWaitingRequests();
    }

    @GetMapping("/finished")
    public List<Request> getFinishedRequests() {
        return storage.getFinishedRequests();
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public void deleteRequest(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam(defaultValue = "true") Boolean fromFinished) {

        List<Request> requests = fromFinished ? storage.getFinishedRequests() : storage.getWaitingRequests();
        final LocalDateTime[] finalDate = { date };
        requests = requests.stream().filter(req -> !req.getCreatedDate().equals(finalDate[0]))
                .collect(Collectors.toList());
        if (fromFinished)
            storage.setFinishedRequests(requests);
        else
            storage.setWaitingRequests(requests);
    }

    @PostMapping(value = "/")
    public Request postMethodName(@RequestParam String url, @RequestParam String keyword,
            @RequestParam(defaultValue = "") String matchUrl) {

        Request newRequest = new Request(url, matchUrl, keyword);
        storage.addWaitingRequest(newRequest);
        return newRequest;
    }
}