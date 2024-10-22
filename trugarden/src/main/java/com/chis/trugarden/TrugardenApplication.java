package com.chis.trugarden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware") // Bean "auditorAware"
public class TrugardenApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrugardenApplication.class, args);
	}

}
