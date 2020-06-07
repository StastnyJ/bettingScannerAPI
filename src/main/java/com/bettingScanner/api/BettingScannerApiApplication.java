package com.bettingScanner.api;

import com.bettingScanner.api.storage.CloudStorage;
import com.bettingScanner.api.storage.Storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BettingScannerApiApplication {

	public static final Storage localStorage = new CloudStorage();

	public static void main(String[] args) {
		SpringApplication.run(BettingScannerApiApplication.class, args);
	}
}
