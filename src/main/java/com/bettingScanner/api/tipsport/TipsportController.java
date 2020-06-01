package com.bettingScanner.api.tipsport;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tipsport/v1")
public class TipsportController {

    @GetMapping("/sessionId")
    public String getSessionId() {
        return "test";
    }
}