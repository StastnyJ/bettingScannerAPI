package com.bettingScanner.api.notifications;

import com.bettingScanner.api.BettingScannerApiApplication;
import com.bettingScanner.api.services.StringSharerService;
import com.bettingScanner.api.storage.Storage;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications/v1")
public class TelegramController {
    final Storage storage = BettingScannerApiApplication.localStorage;

    @GetMapping("/register")
    public ResponseEntity<Void> register(@RequestBody String rawBody) {
        StringSharerService.postString("test", rawBody);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
