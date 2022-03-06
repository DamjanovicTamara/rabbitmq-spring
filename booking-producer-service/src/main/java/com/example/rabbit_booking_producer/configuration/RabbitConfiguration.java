package com.example.rabbit_booking_producer.configuration;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

@EnableRabbit// annotation to enable listener
@Configuration
public class RabbitConfiguration {


    @Value("${spring.rabbitmq.host}")
    String host;

    @Value("${spring.rabbitmq.username}")
    String username;

    @Value("${spring.rabbitmq.password}")
    String password;

    @Value("${spring.rabbitmq.queue}")
    private String queueName;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Bean
    CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        cachingConnectionFactory.setPort(port);
        cachingConnectionFactory.setConnectionTimeout(1000);
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    //Get  message exchange from the properties file
    @Bean("messageExchange")
    public DirectExchange messageExchange() {
        return new DirectExchange(exchange);
    }

    //Get message audit queue from the properties file
    @Bean(name = "messageQueue")
    public Queue messageQueue() {
        return new Queue(queueName, false);
    }

    //Bind message exchange to messageAuditQueue
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(messageQueue()).to(messageExchange()).with(routingkey);
    }

    //Define booking exchange
    @Bean("bookingExchange")
    public DirectExchange bookingExchange() {
        DirectExchange bookingExchange = new DirectExchange("booking.exchange");
        return bookingExchange;
    }

    //Bind booking exchange and message exchange with the routing key from the properties file- message.routingKey - used in above audit binding
    @Bean
    public Binding bindingExchanges() {
        return BindingBuilder.bind(messageExchange()).to(bookingExchange()).with(routingkey);
    }

    //Define booking queues: BookingAdd, BookingEdit and BookingDelete queue
    @Bean(name = "bookingAddQueue")
    public Queue bookingAddQueue() {
        return new Queue("bookingAdd.queue", false);//false as we don't need the queues to be alive on application restart
    }

    @Bean(name = "bookingEditQueue")
    public Queue bookingEditQueue() {
        return new Queue("bookingEdit.queue", false);
    }

    @Bean
    public Queue bookingDeleteQueue() {
        return new Queue("bookingDelete.queue", false);
    }

    //Bind Booking Queues to Booking Exchange
    @Bean
    public Binding bindBookingAdd() {
        return BindingBuilder.bind(bookingAddQueue()).to(bookingExchange()).with("bookingAdd.routingKey");
    }

    @Bean
    public Binding bindBookingEdit() {
        return BindingBuilder.bind(bookingEditQueue()).to(bookingExchange()).with("bookingEdit.routingKey");
    }

    @Bean
    public Binding bindBookingDelete() {
        return BindingBuilder.bind(bookingDeleteQueue()).to(bookingExchange()).with("bookingDelete.routingKey");
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(jsonMessageConverter());
        factory.setErrorHandler(errorHandler());
        return factory;
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ConditionalRejectingErrorHandler(new MyFatalExceptionStrategy());
    }

    public static class MyFatalExceptionStrategy extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {
        private final Logger logger = LogManager.getLogger(getClass());

        @Override
        public boolean isFatal(Throwable t) {
            if (t instanceof ListenerExecutionFailedException) {
                ListenerExecutionFailedException lefe = (ListenerExecutionFailedException) t;
                logger.error("Failed to process inbound message from queue "
                        + lefe.getFailedMessage().getMessageProperties().getConsumerQueue()
                        + "; failed message: " + lefe.getFailedMessage(), t);
            }
            return super.isFatal(t);
        }
    }

}
