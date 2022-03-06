package com.example.rabit_booking_consumer;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableRabbit
@EntityScan("com.example.rabbit_commons.domain")
@EnableJpaRepositories("com.example.rabbit_commons.repository")
@SpringBootApplication
public class BookingConsumerServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(BookingConsumerServiceApplication.class, args);
    }
}
