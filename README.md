# Del+ Finance System
This is a sample application for managing customer accounts. It allows users to create, update, and delete customer and account information.

##Prerequisites
To run this application, you'll need the following:

* Java 8 or higher
* Maven 3.6 or higher

## Getting Started
### Clone the repository:
```bash 
git clone https://github.com/[ER5ATZ]/[delfin].git
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
### GET /api/customers
Retrieves a list of all customers.

### GET /api/customers/{id}
Retrieves a customer by ID.

### POST /api/customers
Creates a new customer.

### PUT /api/customers/{id}
Updates a customer by ID.

### DELETE /api/customers/{id}
Deletes a customer by ID.

## Account API
### GET /api/accounts
Retrieves a list of all accounts.

### GET /api/accounts/{id}
Retrieves an account by ID.

### POST /api/accounts
Creates a new account.

### PUT /api/accounts/{id}
Updates an account by ID.

### DELETE /api/accounts/{id}
Deletes an account by ID.

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
