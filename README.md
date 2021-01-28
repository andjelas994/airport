# Airport Gate Management System

Airport Gate Management System is a REST service that manages available gates for planes to park at an airport.

  - Get assigned gate for provided flight number
  - Update a gate as available

### Prerequisites

- You need Java 8 to run the project

### Running the project

Start the project with ```mvnw.cmd spring-boot:run```.
This will start the server on port 8080 with H2 in-memory database.

### Using the application

There are 2 endpoints available:
- **GET** ```/gate/{flightNumber}``` --- finds first available gate and assigns the flight number to that gate
- **PUT** ```/gate/{gateNumber}``` --- updates the gate as available

### Database

Schema and initial data can be seen in src/main/resources/schema.sql and src/main/resources/data.sql.

### Technology Stack
* Spring MVC
* Spring Boot
* Hibernate ORM
* Lombok
* H2 in-memory database
* JUnit 5

### Running tests
Run tests with ```mvnw.cmd clean test```

