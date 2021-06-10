# Order Products Application

# Table of contents 
* [About the project](#about-the-project)
* [Technologies used](#technologies-used)
* [How to run it?](#how-to-run-it)
* [How to use this API?](#how-to-use-this-api)
    * [Swagger](#swagger)

# About the project
In this repository there are backend part of Order Products web application created for job interview. 
In this project due to REST API user can:
* create, update and show products,
* create and show product record on a order list,
* create and show order lists.

In addition, where product on an order list change it's name or price and that oreder list is closed, then user has to send HTTP request (PATCH method) on endpoint:

```
/api/v1/orders/calculate/{orderId}
```

* ``orderId`` - unique id of an order.

There are also validation for some part of user input and bad activity bring feedback in a form of custom exeptions. 

Project is set up on the requirements of task description. 

# Technologies used
* Java 11
* Spring Boot 2.5.0
* H2 Database 
* Spring Data JPA
* Lombok
* Swagger

# How to run it?

 Firstly you have to download the repository

```cmd
git clone https://github.com/Piotr0W0/OrderProduct.git
 ```

Project was written and tested in IntelliJ IDEA environment. 
You can also run the application in the terminal using maven. To do it you need [maven](https://maven.apache.org/install.html).

In order to run the application you have to go to project directory using
```cmd
cd OrderProduct
```
And then you can run project using
```
mvn spring-boot:run
```

# How to use this API?

* Application by default uses port 8080, to change it edit ``application.properties`` by adding line ``server.port = n`` where n is chosen port. 

## Swagger
Documentation of the project used Swagger. 

