# TransferMoney App

TransferMoney App is a money transfer management application developed with Spring Boot. The application utilizes Spring Security and JWT for authentication and authorization, allowing users to register, log in, deposit money, and make transfers to other registered individuals on the platform.

## Features

- **Registration and Login**: Users can register and log in using JWT for authentication.
- **Money Deposit**: Users can deposit money into their accounts.
- **Money Transfers**: Users can transfer money to other registered users via automatically generated CBU (Clave Bancaria Uniforme) or by username.
- **Minimum Transfer Amount**: The minimum transfer amount is 100 monetary units.
- **Transfer History**: Each user can view the transfers they have made and received.

## Technologies Used

- **Java 17**
- **Spring Boot**
- **Spring Security**
- **JWT (JSON Web Token)**
- **Hibernate**
- **JPA (Java Persistence API)**
- **MySQL (for production)**
- **Maven**
- **JUnit**
- **SpringDoc OpenAPI**
- **Swagger UI**
  
To view the interactive API documentation, visit [Swagger UI](http://localhost:8080/v3/swagger-ui/index.html).

- ## API Endpoints

### Authentication

- **POST /api/auth/register**: Register new users.
- **POST /api/auth/login**: Authenticate existing users.

### Users

- **POST /api/user/deposit**: Deposit money into the user's account.
- **GET /api/user/transfers**: Get transfers sent by the authenticated user.
- **GET /api/user/receivedTransfers**: Get transfers received by the authenticated user.
- **GET /api/user/me**: Get information of the authenticated user.

### Transactions

- **POST /api/transfer/create**: Transfer money to another user by CBU or username.

## Usage Examples
 POST http://localhost:8080/api/auth/register
{
"username":"example1",
"password":"password1",
"mail":"example1@gmail.com",
"firstname":"exampleName",
"lastname":"exampleLastname",
"dni":"000000000"
}

POST http://localhost:8080/api/auth/login
{
"username":"example1",
"password":"password1"
}

POST http://localhost:8080//api/user/deposit
{
    "moneyToDeposit": 500.00
}

POST http://localhost:8080/api/transfer/create
{
  "recipientUser": "example1",
  "transferAmount": 990.50
}

or

{
  "recipientCbu": "00000000000000000000",
  "transferAmount": 990.50
}




