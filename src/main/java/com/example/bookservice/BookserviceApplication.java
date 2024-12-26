package com.example.bookservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEurekaServer
@SpringBootApplication
public class BookserviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookserviceApplication.class, args);
	}
}
