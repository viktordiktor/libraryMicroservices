# Spring (Cloud, MVC, JPA) Microservices with Hibernate, JWT filter, MySQL DB, FlyWay, Swagger docs

---

This application utilizes a microservices approach. It is built using Spring (Cloud, MVC, Data), Hibernate, JWT filter, MySQL DB, FlyWay, documented with Swagger, containerized with Docker (Docker Compose). The application has a well-structured architecture, including an API gateway, Discovery Server, and services such as the book service, library service, and authentication service. Inter-service communication has been implemented, and the Circuit Breaker pattern has been applied.

---

## Quick Start
#### Run app with the help of Docker Compose:

    docker-compose up -d

![Run application](./images/runApp.jpg)
![Containers](./images/runApp2.jpg)

#### Stop application:

    docker stop $(docker ps -a -q)

![Stop application](./images/stopApp.jpg)

---

#### 4) Eureka/Swagger End Points:
    Eureka endpoint: localhost:8080/eureka/web
![Eureka Main Screen](./images/eureka.jpg)

    Swagger endpoint: localhost:8080/swagger-ui.html
![Swagger Main Screen](./images/swagger1.png)
![Swagger Book Service](./images/swagger2.png)
---
#### 5) Requests example
Register request:
![Register Request](./images/request.jpg)

All requests are secured with JWT token.
![Request with JWT](./images/requestJwt.jpg)

---

    



