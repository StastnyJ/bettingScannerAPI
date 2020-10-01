package com.bettingScanner.api.notifications;

import java.util.List;
import java.util.Random;

import com.bettingScanner.api.BettingScannerApiApplication;
import com.bettingScanner.api.services.TelegramService;
import com.bettingScanner.api.storage.Storage;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications/v1")
public class TelegramController {
    final Storage storage = BettingScannerApiApplication.localStorage;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody String rawBody) throws JSONException {
        JSONObject obj = new JSONObject(rawBody);
        JSONObject chat = obj.getJSONObject("message").getJSONObject("chat");
        String name;
        try {
            name = chat.getString("first_name") + " " + chat.getString("last_name");
        } catch (Exception ex) {
            name = "ProxyName," + Integer.toString(new Random().nextInt(9999));
        }
        String id = Integer.toString(chat.getInt("id"));
        storage.addChat(new ChatInfo(id, name));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getChats")
    public List<ChatInfo> getChats() {
        return storage.getChats();
    }

    @PostMapping("/test")
    public ResponseEntity<Void> test(@RequestParam String chatId) {
        TelegramService.testNotification(chatId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
