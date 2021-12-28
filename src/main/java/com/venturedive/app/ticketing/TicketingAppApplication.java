package com.venturedive.app.ticketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan("com.venturedive.app")
public class TicketingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketingAppApplication.class, args);
	}
}
