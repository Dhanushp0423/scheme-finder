package com.example.scheme.finder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SchemeFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchemeFinderApplication.class, args);
	}

}
