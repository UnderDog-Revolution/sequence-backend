package com.seq.deleteAccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//@SpringBootApplication
@SpringBootApplication(exclude = SecurityAutoConfiguration.class) //secure 기본 로그인 비활성화


public class DeleteAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeleteAccountApplication.class, args);
	}

}
