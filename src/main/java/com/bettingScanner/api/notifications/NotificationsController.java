package com.bettingScanner.api.notifications;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import com.bettingScanner.api.services.NotificationService;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications/v1")
public class NotificationsController {

    @Autowired
    private ChatsRepository chatsRepository;

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
        chatsRepository.saveAndFlush(new ChatInfo(id, name, ChatInfo.Platforms.TELEGRAM));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/registerDiscord")
    public String register(@RequestParam String webhook, @RequestParam String name) {
        String id = "D" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSXXX"));
        chatsRepository.saveAndFlush(new ChatInfo(id, name, ChatInfo.Platforms.DISCORD, webhook));
        return id;
    }

    @GetMapping("/getChats")
    public List<ChatInfo> getChats(@RequestParam(required = false, defaultValue = "true") Boolean visibleOnly) {
        return visibleOnly ? chatsRepository.findByVisible(true) : chatsRepository.findAll();
    }

    @PostMapping("/rename")
    public ChatInfo renameChat(@RequestParam String id, @RequestParam String name) {
        ChatInfo chat = chatsRepository.findById(id).orElse(null);
        if (chat == null)
            throw new IllegalArgumentException();

        chat.setName(name);

        chatsRepository.saveAndFlush(chat);
        return chat;
    }

    @PostMapping("/toggleVisibility")
    public ChatInfo toggleVisibility(@RequestParam String id) {
        ChatInfo chat = chatsRepository.findById(id).orElse(null);
        if (chat == null)
            throw new IllegalArgumentException();

        chat.setVisible(!chat.getVisible());

        chatsRepository.saveAndFlush(chat);
        return chat;
    }

    @DeleteMapping("/")
    public void deleteChat(@RequestParam String id) {
        chatsRepository.deleteById(id);
        chatsRepository.flush();
    }

    @PostMapping("/test")
    public ResponseEntity<Void> test(@RequestParam String chatId) {
        NotificationService.testNotification(chatId, chatsRepository);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
