# rabbitmq-spring
Spring boot microservices integration with RabbitMQ performing CRUD operation

Prerequisite
Install RabbitMQ using Docker and open additional ports 5672 for microservices to connect to:
 docker run -d --hostname ecabsrabbit --name rabbit-assignment -e RABBITMQ_DEFAULT_VHOST=/ -p 15672:15672 -p 5672:5672
rabbitmq:3-management

RabbitMQ Management UI can be accesed from 
http://localhost:15672/
username:guest
password:guest

Spring boot microservices Producer and Consumer will communicate using RabbitMQ Message Broker.
Producer Service publish messages to a broker, and Consumer receive them from a queue.

Topology is defined in Configuration classes, with below definitions, using Direct Exchange:

Producer sends messages which are publish to Message Exchange binded to Message Audit Queue using messageAudit routingKey.
Message Exchange is bind to Booking Exchange using messageAudit routing Key.
Booking Exchange is bind with Booking Add, Booking Edit and Booking Delete Queue with rounting keys: bookingAdd, bookingEdit and bookingDelete respectively
Exchanges and Queues are defined in Configuration files of both application. Represented are two ways of defining queue and exhange names- through properties file and declaring them in a class.


Note:
Data manipulation for REST each endpoint for respective define Queue is done from Producer side, as from consumer side values are being lost from Queue as soon the message is received.


