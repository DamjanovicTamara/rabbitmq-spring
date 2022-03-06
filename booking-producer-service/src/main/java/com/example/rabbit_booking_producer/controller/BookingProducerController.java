package com.example.rabbit_booking_producer.controller;

import com.example.rabbit_booking_producer.service.RabbitMQSender;
import com.example.rabbit_commons.domain.Booking;
import com.example.rabbit_commons.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BookingProducerController {

    private RabbitMQSender rabbitMqSender;

    private BookingRepository bookingRepository;

    @Autowired
    public BookingProducerController(RabbitMQSender rabbitMqSender, BookingRepository bookingRepository) {
        this.rabbitMqSender = rabbitMqSender;
        this.bookingRepository = bookingRepository;
    }

    @Value("${app.message}")
    private String message;

    @PostMapping(value = "/message", consumes = "application/json", produces = "application/json")
    public String publishMessageDetails(@RequestBody Booking booking) {
        rabbitMqSender.sendMessage(booking);
        return message;
    }

    @PostMapping(value = "/booking", consumes = "application/json", produces = "application/json")
    public String publishBookingDetails(@RequestBody Booking booking) {
        rabbitMqSender.sendBooking(booking);
        return message + booking;
    }

    @PutMapping(value = "/editBooking/{id}")
    public String publishBookingIDToEdit(@PathVariable("id") Long id) {
        rabbitMqSender.sendEditBookingID(id);
        return message + id;
    }

    @DeleteMapping(value = "/deleteBooking/{id}")
    public String publishBookingIDToDelete(@PathVariable("id") Long id) {
        rabbitMqSender.sendDeleteBookingID(id);
        return message + id;
    }

}
