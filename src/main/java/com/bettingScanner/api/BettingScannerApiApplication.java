package com.bettingScanner.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BettingScannerApiApplication {

	public static final LocalStorage localStorage = new LocalStorage();

	public static void main(String[] args) {
		SpringApplication.run(BettingScannerApiApplication.class, args);
	}
}
