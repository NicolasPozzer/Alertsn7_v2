package com.demo.alertsn7_v2_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlertsN7BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlertsN7BackendApplication.class, args);
	}

}
