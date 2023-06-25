[![Java CI with Gradle](https://github.com/samabcde/electronic-store/actions/workflows/gradle.yml/badge.svg)](https://github.com/samabcde/electronic-store/actions/workflows/gradle.yml)

# electronic-store

Java Spring Boot Project to demonstrate a Electronic Store Application

## Function

### Admin User Operations

- Create a new product
- Remove a product
- Add discount deals for products (Example: Buy 1 get 50% off the second)

### Customer Operations

- Add and remove products to and from a basket
- Calculate a receipt of items, including all purchases, deals applied and total price

## Build And Run

### Prerequisite

JDK 17 is installed

### Run Test

`./gradlew test`

### Run Application

`./gradlew bootRun`

### Build JAR

`./gradlew build`

### Swagger UI

After application started,  
open [swagger ui page](http://localhost:8080/swagger-ui.html)
