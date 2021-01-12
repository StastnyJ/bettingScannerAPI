package com.bettingScanner.api.requests;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bettingScanner.api.services.TelegramService;
import com.bettingScanner.api.services.WebScanningService;
import com.bettingScanner.api.tipsport.Match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/requests/v1")
public class RequestsController {
    @Autowired
    RequestsRepository requestsRepository;

    @GetMapping("/all")
    public List<Request> getAllRequests(@RequestParam(required = false, defaultValue = "true") Boolean visibleOnly) {
        List<Request> res = requestsRepository.findAll().stream().sorted((a, b) -> {
            if (!a.isFinnished() && b.isFinnished())
                return -1;
            if (a.isFinnished() && !b.isFinnished())
                return 1;
            return 0;
        }).collect(Collectors.toList());
        return visibleOnly ? filterInvisible(res) : res;
    }

    @GetMapping("/waiting")
    public List<Request> getWaitingRequests(
            @RequestParam(required = false, defaultValue = "true") Boolean visibleOnly) {
        List<Request> res = requestsRepository.findByFinnished(false);
        return visibleOnly ? filterInvisible(res) : res;

    }

    @GetMapping("/finished")
    public List<Request> getFinishedRequests(
            @RequestParam(required = false, defaultValue = "true") Boolean visibleOnly) {

        List<Request> res = requestsRepository.findByFinnished(true);
        return visibleOnly ? filterInvisible(res) : res;
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public void deleteRequest(@RequestParam Integer id) {
        requestsRepository.deleteById(id);
    }

    @PostMapping(value = "/")
    public Request addRequest(@RequestParam String url, @RequestParam String keyword, @RequestParam String email,
            @RequestParam(defaultValue = "") String matchUrl) {

        Request newRequest = new Request(url, matchUrl, keyword, email);
        requestsRepository.save(newRequest);
        return newRequest;
    }

    @PostMapping(value = "/withStatus")
    public Request addStateRequest(@RequestParam String url, @RequestParam String category, @RequestParam String email,
            @RequestParam(defaultValue = "") String matchUrl) {

        Request newRequest = new Request(url, matchUrl, email, category, "");
        requestsRepository.save(newRequest);
        return newRequest;
    }

    @PostMapping("/toggleVisibility")
    public Request toggleVisibility(@RequestParam Integer id) {
        Request req = requestsRepository.findById(id).orElse(null);
        if (req == null)
            throw new IllegalArgumentException();

        req.setVisibe(!req.isVisibe());

        requestsRepository.saveAndFlush(req);
        return req;
    }

    @PostMapping(value = "/scan")
    public List<Request> scan() {
        List<Request> requests = getWaitingRequests(false);
        List<Request> result = new ArrayList<>();
        List<List<Match>> stateResult = new ArrayList<>();
        for (Request act : requests) {
            try {
                if (act.getRequestType().equals("STATE")) {
                    List<Match> changes = WebScanningService.scanStateRequest(act);
                    if (changes.size() > 0) {
                        stateResult.add(changes);
                        TelegramService.notifyStateChange(changes, act.getChatId());
                        requestsRepository.save(act);
                    }
                } else {
                    if (WebScanningService.scanRequest(act)) {
                        result.add(act);
                        act.setFinnished(true);
                        requestsRepository.save(act);
                    }
                }
            } catch (MalformedURLException ex) {
                requestsRepository.deleteById(act.getId());
            }
        }
        if (result.size() > 0)
            TelegramService.notifyFounds(result);
        requestsRepository.flush();
        return result;
    }

    private List<Request> filterInvisible(List<Request> all) {
        return all.stream().filter(r -> r.isVisibe()).collect(Collectors.toList());
    }
}