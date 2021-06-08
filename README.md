# Order Products Application

# Table of contents 
* [About the project](#about-the-project)
* [Technologies used](#technologies-used)
* [How to run it?](#how-to-run-it)
* [How to use this API?](#how-to-use-this-api)
    * [Swagger](#swagger)

# About the project
In this repository there are backend part of Order Products web application created of job interview. 

# Technologies used
* Java 11
* Spring Boot 2.5.0
* H2 Database 
* PostgreSQL 13.2
* Spring Data JPA
* Lombok
* Swagger

# How to run it?

 Firstly you have to download the repository

```cmd
git clone https://github.com/Piotr0W0/OrderProduct.git
 ```

You can run the application in the terminal. To do it you need [maven](https://maven.apache.org/install.html).

In order run run the application go to project directory and there run the app using

```
mvn spring-boot:run
```

# How to use this API?

* Application by default uses port 8080, to change it edit ``application.properties`` by adding line ``server.port = n`` where n is chosen port. 

## Swagger
Documentation of the project used Swagger UI. 
