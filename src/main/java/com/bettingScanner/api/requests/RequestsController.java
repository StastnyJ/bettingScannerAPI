package com.bettingScanner.api.requests;

import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.bettingScanner.api.notifications.ChatsRepository;
import com.bettingScanner.api.services.NotificationService;
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

    @Autowired
    ChatsRepository chatsRepository;

    @GetMapping("/all")
    public List<Request> getAllRequests(@RequestParam(required = false, defaultValue = "true") Boolean visibleOnly) {
        List<Request> res = requestsRepository.findAll().stream().sorted((a, b) -> {
            if (!a.getFinnished() && b.getFinnished())
                return -1;
            if (a.getFinnished() && !b.getFinnished())
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

    @PostMapping(value = "/repeatedKeyword")
    public Request addRepeatedKeywordRequest(@RequestParam String url, @RequestParam String category,
            @RequestParam String email, @RequestParam String keyword,
            @RequestParam(defaultValue = "") String matchUrl) {

        Request newRequest = new Request(url, matchUrl, keyword, email, null, false, true, LocalDate.now(), category,
                "", "REPEATED");
        requestsRepository.save(newRequest);
        return newRequest;
    }

    @PostMapping("/toggleVisibility")
    public Request toggleVisibility(@RequestParam Integer id) {
        Request req = requestsRepository.findById(id).orElse(null);
        if (req == null)
            throw new IllegalArgumentException();

        req.setVisibe(!req.getVisibe());

        requestsRepository.saveAndFlush(req);
        return req;
    }

    @PostMapping("/clear")
    public void clear() {
        requestsRepository.clearGenerated();
        requestsRepository.resetStates();
    }

    @PostMapping(value = "/scan")
    public String scan() {
        List<Request> requests = getWaitingRequests(false);
        List<Request> result = Collections.synchronizedList(new ArrayList<>());
        List<List<Match>> stateResult = Collections.synchronizedList(new ArrayList<>());
        List<String> errors = Collections.synchronizedList(new ArrayList<>());
        requests.stream().parallel().forEach(act -> {
            try {
                Request updated = requestsRepository.findById(act.getId()).orElse(null);
                if (updated != null && updated.getFinnished())
                    return;
                WebScanningService.tipsportJSessionId = WebScanningService
                        .getJSessionId(new URL("https://www.tipsport.cz/"));
                if (act.getRequestType().equals("STATE") || act.getRequestType().equals("REPEATED")) {
                    List<Match> changes = WebScanningService.scanStateRequest(act);
                    if (act.getRequestType().equals("STATE")) {
                        if (changes.size() > 0) {
                            stateResult.add(changes);
                            NotificationService.notifyStateChange(changes, act.getChatId(), chatsRepository);
                            requestsRepository.saveAndFlush(act);
                        }
                    } else {
                        for (Match change : changes) {

                            Request req = new Request(
                                    "https://m.tipsport.cz/rest/offer/v1/matches/" + change.getId()
                                            + "/event-tables?fromResults=false",
                                    change.getMatchUrl(), act.getKeyword(), act.getChatId(), null, false, true,
                                    LocalDate.now(), "", "", "GENERATED");
                            requestsRepository.saveAndFlush(req);
                            if (WebScanningService.scanRequest(req)) {
                                result.add(req);
                                req.setFinnished(true);
                                requestsRepository.saveAndFlush(req);
                            }
                        }
                    }
                } else {
                    if (WebScanningService.scanRequest(act)) {
                        result.add(act);
                        act.setFinnished(true);
                        requestsRepository.saveAndFlush(act);
                    }
                }
            } catch (MalformedURLException ex) {
                requestsRepository.deleteById(act.getId());
            } catch (Exception ex) {
                errors.add("[ERROR] " + act.getId() + " " + ex.getMessage());
            }
        });
        if (result.size() > 0)
            NotificationService.notifyFounds(result, chatsRepository);
        requestsRepository.flush();
        return String.join("\n", errors) + " " + result.size() + " requests found, " + requests.size() + "scanned";
    }

    @PostMapping("/testScan")
    public String testScan(Integer requestId) throws MalformedURLException {
        Request req = requestsRepository.findById(requestId).orElse(null);
        if (req == null)
            return "Req. Not found";
        String tipsportJSessionId = WebScanningService.getJSessionId(new URL("https://www.tipsport.cz/"));
        return "ID: " + tipsportJSessionId + "\nContent:" + WebScanningService.getSiteContent(new URL(req.getScanUrl()),
                new HttpCookie("JSESSIONID", tipsportJSessionId)) + "\n";
    }

    private List<Request> filterInvisible(List<Request> all) {
        return all.stream().filter(r -> r.getVisibe()).collect(Collectors.toList());
    }
}