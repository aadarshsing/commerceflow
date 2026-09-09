# CommerceFlow

CommerceFlow is a **backend e-commerce system** built using **Spring Boot and microservices architecture**. The system is designed to handle core e-commerce operations such as products, categories, sellers, inventory, orders, payments, and service-to-service communication.

## 🏗️ Architecture

CommerceFlow follows a microservices-based architecture where each service is responsible for a specific business capability.

```text
                         ┌─────────────────┐
                         │   API Gateway    │
                         └────────┬────────┘
                                  │
              ┌───────────────────┼───────────────────┐
              │                   │                   │
              ▼                   ▼                   ▼
       ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
       │   Product   │     │    Order    │     │   Payment   │
       │   Service   │     │   Service   │     │   Service   │
       └──────┬──────┘     └──────┬──────┘     └─────────────┘
              │                   │
              ▼                   ▼
       ┌─────────────┐     ┌─────────────┐
       │  Inventory  │     │   Payment   │
       │   Service   │     │   Service   │
       └─────────────┘     └─────────────┘

                    ┌─────────────────────┐
                    │   Eureka Server     │
                    │ Service Discovery   │
                    └─────────────────────┘
```

## 🚀 Services

The application is divided into multiple services:

* **Eureka Server** – Service registry and service discovery.
* **API Gateway** – Single entry point for client requests and routing.
* **Product Service** – Manages products, categories, and sellers.
* **Inventory Service** – Manages product stock, available quantity, reserved quantity, and low-stock thresholds.
* **Order Service** – Handles order creation, order lifecycle, and order-related operations.
* **Payment Service** – Handles payments, payment status, transaction references, and refunds/cancellations.

## 🛠️ Technologies

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* Spring Cloud
* Netflix Eureka
* Spring Cloud Gateway
* OpenFeign
* MySQL
* Maven
* REST APIs
* Bean Validation

## 🔗 Service Communication

Services communicate with each other using REST APIs and **OpenFeign**.

For example:

```text
Order Service
      │
      ├──► Product Service
      │
      ├──► Inventory Service
      │
      └──► Payment Service
```

Eureka is used for service discovery so services can locate each other without hardcoding service locations.

## 🗄️ Database

The application uses **MySQL** with Spring Data JPA and Hibernate.

Each service can maintain its own database/schema according to its business responsibility.

Example:

```text
Product Service  → productDb
Inventory Service → inventoryDb
Order Service    → orderDb
Payment Service  → paymentDb
```

## 📦 Main Features

### Product Management

* Create and update products
* Product categories
* Parent and child categories
* Seller management
* Product filtering and pagination
* Product status management

### Inventory Management

* Create inventory for products
* Track available quantity
* Track reserved quantity
* Configure low-stock thresholds
* Inventory validation during order processing

### Order Management

* Create orders
* Manage order status
* Validate product and inventory information
* Communicate with inventory and payment services

### Payment Management

* Payment creation
* Transaction reference generation
* Payment status management
* Payment cancellation
* Refund handling

## 🔍 Service Discovery

The Eureka Server runs on:

```text
http://localhost:8761
```

Services register themselves with Eureka when they start.

Example:

```text
EUREKA SERVER
     │
     ├── PRODUCT
     ├── INVENTORY
     ├── ORDER
     └── PAYMENT
```

## ▶️ Running the Application

### Prerequisites

Make sure you have installed:

* Java
* Maven
* MySQL

### Start the services

Start the Eureka Server first:

```bash
mvn spring-boot:run
```

Then start the remaining microservices.

Verify the Eureka dashboard:

```text
http://localhost:8761
```

## 📌 Project Status

CommerceFlow is an ongoing backend project focused on implementing a real-world **e-commerce microservices architecture** with service discovery, inter-service communication, database management, transactions, inventory management, orders, and payments.

## 🔮 Future Improvements

Planned improvements include:

* Event-driven communication
* Message broker integration
* Distributed transaction handling
* Resilience and retry mechanisms
* Centralized configuration
* Authentication and authorization
* Distributed tracing
* Monitoring and observability
* Improved fault tolerance

## 👨‍💻 Purpose

CommerceFlow is built as a practical project to understand and implement **production-style Spring Boot microservices architecture** and common challenges involved in distributed systems.
