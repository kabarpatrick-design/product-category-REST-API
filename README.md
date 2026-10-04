# Product Category REST API

A Spring Boot REST API for managing products and product categories, backed by MySQL.

> **Status:** early setup. The application skeleton, database connection and API docs are configured; entities, repositories and controllers are not added yet.

## Tech stack

- Java 21
- Spring Boot 4.1 (Web MVC, Data JPA, Validation, DevTools)
- MySQL (via `mysql-connector-j`)
- Lombok
- springdoc-openapi (Swagger UI)
- Maven (wrapper included)

## Prerequisites

- JDK 21
- MySQL running on `localhost:3306`

Maven does not need to be installed; use the included wrapper (`mvnw` / `mvnw.cmd`).

## Configuration

Database settings are in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/productschema?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

- The `productschema` database is created automatically on first run.
- Tables are created and updated from the JPA entities (`ddl-auto=update`).
- Change the username and password to match your local MySQL setup.

## Running the app

Windows:

```
mvnw.cmd spring-boot:run
```

macOS / Linux:

```
./mvnw spring-boot:run
```

The API starts on http://localhost:8080.

## API documentation

With the app running, open Swagger UI at:

- http://localhost:8080/swagger-ui.html

The raw OpenAPI spec is at http://localhost:8080/v3/api-docs.

## Running tests

```
mvnw.cmd test
```

(or `./mvnw test` on macOS / Linux). The tests start the full application context, so MySQL must be running.

## Project structure

```
src/
  main/
    java/com/chriskar/product/
      ProductApplication.java   # Spring Boot entry point
    resources/
      application.properties    # App and database configuration
  test/
    java/com/chriskar/product/
      ProductApplicationTests.java
pom.xml                         # Maven build and dependencies
```
