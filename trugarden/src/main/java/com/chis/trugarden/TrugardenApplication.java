package com.chis.trugarden;

import com.chis.trugarden.persistence.role.entities.RoleEntity;
import com.chis.trugarden.persistence.role.RoleJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware") // Bean "auditorAware"
@EnableAsync
public class TrugardenApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrugardenApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleJpaRepository roleJpaRepository) {
		return args -> {
			if(roleJpaRepository.findByName("USER").isEmpty()) {
				roleJpaRepository.save(RoleEntity.builder().name("USER").build());
			}
		};
	}

}
