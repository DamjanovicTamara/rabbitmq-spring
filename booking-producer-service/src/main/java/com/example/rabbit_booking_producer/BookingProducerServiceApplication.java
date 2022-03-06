package com.example.rabbit_booking_producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.example.rabbit_commons.domain")
@EnableJpaRepositories("com.example.rabbit_commons.repository")

public class BookingProducerServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(BookingProducerServiceApplication.class, args);
    }
}
