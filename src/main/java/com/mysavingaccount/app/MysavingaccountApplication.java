package com.mysavingaccount.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MysavingaccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(MysavingaccountApplication.class, args);
	}

}
