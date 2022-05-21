package ru.kuranov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.kuranov")
public class CrossSubsetsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrossSubsetsApplication.class, args);
	}

}
