# Del/Finanz System
This is a simple application for managing customer accounts. It allows users to create, update, and delete customer and account information and add and view transactions.

## Prerequisites
To run this application, you'll need the following:

* Java 8 or higher
* Maven 3.6 or higher

## Getting Started
### Clone the repository:
```bash 
git clone https://github.com/ER5ATZ/delfin.git
```

### Create a database and update the application.properties file with the database URL, username, and password:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/[database-name]
spring.datasource.username=[username]
spring.datasource.password=[password]
```

### Run the application:
```bash
mvn spring-boot:run
```
Open the application in your browser at http://localhost:8080.


## Usage Customer API
### GET /api/customer/{id}
Retrieves a customer by ID.

### POST /api/customer/
Creates a new customer.

### PUT /api/customers/{id}
Updates a customer by ID.

### DELETE /api/customer/{id}
Deletes a customer by ID.

## Account API
### GET /api/account/{id}
Retrieves an account by ID.

### POST /api/account/
Creates a new account.

### PUT /api/account/{id}
Updates an account by ID.

### DELETE /api/account/{id}
Deletes an account by ID.

## Transaction API
### POST /api/transaction/
Generates a new transaction.

### GET /api/transaction/{id}
Retrieves a transaction by ID.

## Technologies Used
* Java 8
* Spring Boot
* Spring Data JPA
* H2
* Maven

### License
This project is licensed under the MIT License. See the LICENSE file for details.

### Acknowledgements
This application was created as a sample project for educational purposes only. It is not intended for commercial use.
