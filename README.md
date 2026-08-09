# Del/Finanz System

A Spring Boot application for managing customer accounts, transactions, and user authentication.

## Prerequisites

* Java 21 or higher
* Maven 3.9+ (wrapper included: `./mvnw`)

## Getting Started

### Clone the repository:
```bash
git clone https://github.com/ER5ATZ/delfin.git
cd delfin
```

### Run the application:
```bash
./mvnw spring-boot:run
```

The application uses an embedded H2 database by default. Open http://localhost:8080.

### Run with dev profile (verbose logging):
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Run tests:
```bash
./mvnw test
```

## API Endpoints

### Customer API
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/customer/{id}` | Retrieve customer by ID |
| POST | `/api/customer/` | Create a new customer (201) |
| PUT | `/api/customer/` | Update a customer |
| DELETE | `/api/customer/{id}` | Delete a customer |

### Account API
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/account/{id}` | Retrieve account by ID |
| POST | `/api/account/` | Create a new account (201) |
| PUT | `/api/account/` | Update an account |
| DELETE | `/api/account/{id}` | Delete an account |

### Transaction API
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/transaction/{id}` | Retrieve transaction by ID |
| POST | `/api/transaction/` | Create a new transaction (201) |

### API Documentation
Swagger UI is available at http://localhost:8080/swagger-ui.html when the application is running.

## Technologies

* Java 21
* Spring Boot 3.5.14
* Spring Data JPA
* Spring HATEOAS
* Spring Security
* H2 (embedded, development)
* Flyway (database migrations)
* Springdoc OpenAPI (Swagger UI)
* Lombok
* Maven

## License

This project is licensed under the MIT License. See the LICENSE file for details.
