# Online Banking System

StudentName : Yin San
Course: Spring Boot Development
Date: [2/10/2025]

## 1. 🤑 Features
  * User authentication & authorization
  * Restful account management
  * JWT authentication
  * assign role for user
  * Error message Handling
  * Transaction
  * Transfer Validation

##  2. 🎯 Technical Architecture
  * Security: Spring security with JWT
  * Database: PostgreSQL
  * Build Tool: Maven
  * Java Version: 21

## 3. 🎁 Project Installation
🚀 Git Clone Repository
```
git clone https://github.com/YinsanGit/online_banking_assignment.git
```
## 4. 🛠️ Configure Database
  #### application.yml:
  ```
  server:
  port: 8080
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/simple_bank_db
    username: postgres
    password: 1234
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
  ```

## 5. 🎨 Database Design implementation
  * User : Store Credentails with role premission
  * role : Only admin can define role for user
  * permission : Defines new permissions
  * Account : Manage account information
  * RolePermission : Many To Many relationship between role and permission
  * Transaction : Transfer

## 6. 📍 API Endpoint Implementation
  #### Authentication
| Method | Endpoint | Description | Access |
| ------ | -------- | ----------- | ------ |
| POST   | /api/auth/register | User Registation | ADMIN |
| POST   | /api/auth/login | User Login | PUBLIC |


## 7. 📍 User Management with role
| Method | Endpoint | Description | Access |
| ------ | -------- | ----------- | ------ |
| GET    | api/users/with-roles | Get All User with role | ADMIN |
| GET    | /api/users/{id}/roles | Get By ID with role | ADMIN |
| Delete | /api/auth/delete/{id} | Delete By ID | ADMIN |


## 8. 📍 Account Management
| Method | Endpoint | Description | Access |
| ------ | -------- | ----------- | ------ |
| POST   | /accounts | Create Account | MANAGER | 
| GET    | /acccounts/getAll | Get All Accounts | MANAGER |
| GET    | /accounts/{id} | Get By Id | MANAGER
| PUT    | /accounts/{id} | Update Account | MANAGER |
| DELETE | /accounts/{id} | delete Account | MANAGER |


## 9. 📍 Transaction Management
| Method | Endpoint | Description | Access |
| ------ | -------- | ----------- | ------ |
| POST   | /api/transfer | Process transfer | PUBLIC | 





  
