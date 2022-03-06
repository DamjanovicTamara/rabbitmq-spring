package com.example.rabbit_booking_producer.service;


import com.example.rabbit_commons.domain.Booking;
import com.example.rabbit_commons.domain.TripWaypoint;
import com.example.rabbit_commons.repository.BookingRepository;
import com.example.rabbit_commons.repository.TripWaypointRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
public class RabbitMQSender {

    private RabbitTemplate rabbitTemplate;

    private BookingRepository bookingRepository;

    private TripWaypointRepository tripWaypointRepository;

    @Autowired
    @Qualifier("messageQueue")
    private Queue queue;

    @Autowired
    @Qualifier("bookingAddQueue")
    private Queue bookingAddQueue;

    @Autowired
    @Qualifier("bookingEditQueue")
    private Queue bookingEditQueue;

    @Autowired
    @Qualifier("bookingDeleteQueue")
    private Queue bookingDeleteQueue;

    @Autowired
    public RabbitMQSender(RabbitTemplate rabbitTemplate, BookingRepository bookingRepository, TripWaypointRepository tripWaypointRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.bookingRepository = bookingRepository;
        this.tripWaypointRepository = tripWaypointRepository;
    }

    private static Logger logger = LogManager.getLogger(RabbitMQSender.class.toString());


    public void sendMessage(Booking booking) {
        rabbitTemplate.convertAndSend(queue.getName(), booking);
        logger.info("Sending Message to the Messaging Queue : " + booking);
    }

    public void sendBooking(Booking booking) {
        bookingRepository.save(booking);
        booking.getTripWaypointList().forEach(t -> {
            t.setBooking(booking);
            tripWaypointRepository.save(t);
        });
        rabbitTemplate.convertAndSend(bookingAddQueue.getName(), booking);
        logger.info("Sending id of booking to the queue " + booking);
    }

    public void sendEditBookingID(Long id) {
        Booking booking = bookingRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException(("Entity with provided id was not found in database " + id)));
        booking.setLastModifiedOn(LocalDateTime.now());
        bookingRepository.save(booking);
        rabbitTemplate.convertAndSend(bookingEditQueue.getName(), booking);
        logger.info("Sending id of booking to be edited to the queue with id: " + id + "details:" + booking);
    }

    public void sendDeleteBookingID(Long id) {
        Booking booking = bookingRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Entity with provided id was not found in database " + id));
        bookingRepository.delete(booking);
        rabbitTemplate.convertAndSend(bookingDeleteQueue.getName(), booking);
        logger.info("Sending id of booking to be deleted to the queue with id: " + id + "details: " + booking);
    }
}
