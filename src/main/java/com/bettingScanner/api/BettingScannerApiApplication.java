package com.bettingScanner.api;

import com.bettingScanner.api.storage.LocalStorage;
import com.bettingScanner.api.storage.Storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BettingScannerApiApplication {

	public static final Storage localStorage = new LocalStorage();

	public static void main(String[] args) {
		SpringApplication.run(BettingScannerApiApplication.class, args);
	}
}
