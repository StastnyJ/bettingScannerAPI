package com.bettingScanner.api.emails;

import java.util.List;
import java.util.regex.Pattern;

import com.bettingScanner.api.BettingScannerApiApplication;
import com.bettingScanner.api.services.EmailingService;
import com.bettingScanner.api.storage.Storage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails/v1")
public class EmailsController {
    final Storage storage = BettingScannerApiApplication.localStorage;
    private static final Pattern mailRegex = Pattern.compile("^.+@.+\\..+$");

    @GetMapping("/")
    public List<String> getEmails() {
        return storage.getEmails();
    }

    @PostMapping("/")
    public ResponseEntity<Void> changeEmail(@RequestParam String email) {
        if (email == null || !mailRegex.matcher(email).matches())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        storage.addEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<Void> deleteEmail(@RequestParam String email) {
        if (email == null || !mailRegex.matcher(email).matches())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        storage.removeEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/test")
    public ResponseEntity<Void> testEmail(@RequestParam String email) {
        if (email == null || !mailRegex.matcher(email).matches())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        EmailingService.testEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}