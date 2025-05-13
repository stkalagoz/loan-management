#  💸 Loan Management API

#### A Spring Boot-based REST API that manages loan creation, installment tracking, and payment operations. The application uses an in-memory H2 database for simplicity and supports Docker for easy deployment.

### 🚀 Features
✅ Create loans with interest and installment plans

✅ Filter existing loans with dynamic query support

✅ Pay multiple installments with reward/penalty logic

✅ View all installments of a specific loan

🔐 Secured with HTTP Basic Authentication


### 🧱 Tech Stack
Java 21

Spring Boot 3.x

Spring Security

Spring Data JPA + H2 (In-memory)

Docker (Alpine JDK)

Maven

###  ⚙️ Build & Run

### ▶️ Run Locally

./mvnw clean package

java -jar target/loan-management-0.0.1-SNAPSHOT.jar

### 🐳 Run in Docker

docker build -t loan-app .

docker run -p 8080:8080 loan-app

### 🔐 Authentication

All endpoints are secured using HTTP Basic Auth.

#### Username: admin

#### Password: password

### 🌐 H2 Console
URL: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:mydb

#### Username: sa

#### Password: (leave blank)



### 📌 REST API Endpoints

#### 🔍 Filter Loans

GET /api/v1/loan/filter

#### Request:

```
{
"filterList": [
{
"key": "customer.id",
"operation": "eq",
"value": 1
}
]
}
```
#### Response:
```
[
{
"id": 101,
"loanAmount": 10000,
"isPaid": false,
"customer": {
"id": 1,
"name": "John Doe"
}
}
]
```
### 📝 Create Loan

#### POST /api/v1/loan/create

#### Request:

```
{
"customerId": 1,
"amount": 10000,
"interestRate": 0.12,
"numberOfInstallments": 6
}
```

#### Response:

```
{
"id": 101,
"loanAmount": 10000,
"numberOfInstallment": 6,
"createDate": "2025-05-13",
"isPaid": false,
"installments": [...]
}

```
💰 Pay Installments
POST /api/v1/loan/pay

#### Request:

```
{
"loanId": 101,
"incomingPayment": 5000
}

```
#### Response:

```
{
"installmentsPaid": 3,
"totalPaid": 5000,
"loanCompletelyPaid": false
}

```
📋 List Installments
GET /api/v1/loan/list/installments

#### Request:

```
1
```

#### Response:

```
[
{
"id": 1,
"amount": 1700,
"paidAmount": 1700,
"dueDate": "2025-06-01",
"paymentDate": "2025-05-13",
"isPaid": true
},
{
"id": 2,
"amount": 1700,
"paidAmount": 0,
"dueDate": "2025-07-01",
"paymentDate": null,
"isPaid": false
}
]
```

## 🧪 Postman Collection

You can import and test the API using the provided Postman collection:

➡️ [Download loan-management.postman_collection.json](./loan-management.postman_collection.json)

### 🔽 How to Use

1. Open [Postman](https://www.postman.com/)
2. Click **Import**
3. Select the file: `loan-management.postman_collection.json`
4. Use these credentials:
    - Username: `admin`
    - Password: `password`

### 📒 Notes

Reward is applied if paid before due date:
discount = amount × 0.001 × daysEarly

Penalty is applied if paid after due date:
penalty = amount × 0.001 × daysLate

Installments must be paid fully — partial payments are not accepted

CSRF is disabled for API and /h2-console

H2 data is volatile (resets on restart)

