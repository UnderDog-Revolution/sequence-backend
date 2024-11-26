package com.example.SequenceBoard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.SequenceBoard.repository")
@EntityScan(basePackages = "com.example.SequenceBoard.entity")
@EnableFeignClients
public class SequenceBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SequenceBoardApplication.class, args);
	}

}
