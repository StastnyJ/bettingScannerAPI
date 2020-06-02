package com.bettingScanner.api.tipsport;

import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bettingScanner.api.services.WebScanningService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tipsport/v1")
public class TipsportController {

    @GetMapping("/token")
    public String getToken() {

        String res = "";
        try {
            return WebScanningService.getJSessionId(new URL("https://www.tipsport.cz/"));
        } catch (MalformedURLException ex) {
            res = "";
        }
        return res;
    }

    @GetMapping("/matches")
    public List<Match> getMatches(@RequestParam String url) {
        Integer id = Integer.parseInt(url.split("-")[url.split("-").length - 1]);
        Map<String, String> params = new HashMap<>();
        params.put("url", url);
        params.put("id", id.toString());
        params.put("results", "false");
        params.put("type", "COMPETITION");
        try {
            String sessionId = WebScanningService.getJSessionId(new URL("https://www.tipsport.cz/"));
            String res = WebScanningService.getSiteContent(new URL("https://m.tipsport.cz/rest/offer/v1/offer"), params,
                    new HttpCookie("JSESSIONID", sessionId));
            return Match.parseFromJson(res);
        } catch (MalformedURLException ex) {
        }
        return null;
    }
}