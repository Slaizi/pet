package ru.Bogachev.pet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class PetApplication {
	public static void main(String[] args) {
		SpringApplication.run(PetApplication.class, args);
	}
}
