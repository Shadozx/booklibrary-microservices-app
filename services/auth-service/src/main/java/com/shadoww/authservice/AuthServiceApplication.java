package com.shadoww.authservice;

import com.shadoww.authservice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AuthServiceApplication implements CommandLineRunner {

	private final PersonService personService;

	@Autowired
	public AuthServiceApplication(PersonService personService) {
		this.personService = personService;
	}

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (!personService.existSUPERADMIN()) {
			personService.createSUPERADMIN();
		}
	}
}
