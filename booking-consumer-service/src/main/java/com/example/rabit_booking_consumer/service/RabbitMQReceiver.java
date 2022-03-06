package com.example.rabit_booking_consumer.service;

import com.example.rabbit_commons.domain.Booking;
import com.example.rabbit_commons.repository.BookingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RabbitMQReceiver implements RabbitListenerConfigurer {

    private static final Logger logger = LogManager.getLogger(RabbitMQReceiver.class);

    private BookingRepository bookingRepository;

    @Autowired
    public RabbitMQReceiver(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue}", containerFactory = "rabbitListenerFactory")
    public void receivedMessage(Booking booking) {
        logger.info("Booking Details Received is.. " + booking);
    }

    @RabbitListener(queues = "bookingAdd.queue")
    public void receivedBooking(Booking booking) {
        logger.info("Booking Details Received is.. " + booking);
    }

    @RabbitListener(queues = "bookingEdit.queue")
    public void receivedBookingToEdit(Booking booking) {
        logger.info("Booking to edit with id:" + booking.getId() + "was received,details: " + booking);

    }

    @RabbitListener(queues = "bookingDelete.queue")
    public void receivedBookingToDelete(Booking booking) {
        logger.info("Booking to delete with id:" + booking.getId() + "was received, details: " + booking);

    }
}
