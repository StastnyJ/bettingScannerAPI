package com.bettingScanner.api.requests;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/all")
    public List<Request> getAllRequests() {
        return FileSerivce.loadWaitingRequests();
    }

    @GetMapping("/finished")
    public List<Request> getFinishedRequests() {
        return FileSerivce.loadFinishedRequests();
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public void deleteRequest(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam(defaultValue = "true") Boolean fromFinished) {

        List<Request> requests = fromFinished ? FileSerivce.loadFinishedRequests() : FileSerivce.loadWaitingRequests();
        final LocalDateTime[] finalDate = { date };
        requests = requests.stream().filter(req -> !req.getCreatedDate().equals(finalDate[0]))
                .collect(Collectors.toList());
        if (fromFinished)
            FileSerivce.saveFinishedRequests(requests);
        else
            FileSerivce.saveWaitingRequests(requests);
    }

    @PostMapping(value = "/")
    public Request postMethodName(@RequestParam String url, @RequestParam String keyword,
            @RequestParam(defaultValue = "") String matchUrl) {

        Request newRequest = new Request(url, matchUrl, keyword);
        FileSerivce.addWaitingRequest(newRequest);
        return newRequest;
    }
}