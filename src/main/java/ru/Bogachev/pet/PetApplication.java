package ru.Bogachev.pet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
public class PetApplication {
    public static void main(final String[] args) {
        SpringApplication.run(PetApplication.class, args);
    }
}
