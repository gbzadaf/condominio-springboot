package com.gabrielf.condoaccess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CondoaccessApplication {

	public static void main(String[] args) {
		SpringApplication.run(CondoaccessApplication.class, args);
	}

}
