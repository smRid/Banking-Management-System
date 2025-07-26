# 🏦 Banking Management System

A backend banking system built with **Java Spring Boot** and **MySQL**, designed to simulate core banking functionalities such as account creation, fund transfers, balance inquiry, and bank statement generation. It also includes **email notifications** for transactions.

---

## 🚀 Features

### 🧾 User Account Management
- Create new bank accounts.
- Retrieve account balance.
- Perform name enquiry using account number.
- Receive account-related email alerts.

### 💰 Transactions
- Credit an account.
- Debit an account.
- Transfer funds between accounts.
- Log and persist all transactions.
- Email notifications for each transaction.

### 📄 Bank Statements
- Generate bank statements for a given account within a specified date range.

---


## 🛠️ Tech Stack

### Backend:
- **Java 11+**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Mail**

### Database:
- **MySQL**

### Tools & Utilities:
- **Lombok** – Reduces boilerplate code.
- **Maven** – Project build and dependency management.

---

## ⚙️ Prerequisites

Ensure the following are installed and configured on your system:

- Java 11 or later  
- Maven  
- MySQL  
- SMTP credentials for email service  

---


## 📡 API Endpoints

### 👤 User Management

#### ➕ Create Account  
**POST** `/api/v1/user/create`  
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "1234567890",
  "alternativePhoneNumber": "0987654321",
  "address": "123 Main St",
  "stateOfOrigin": "New York",
  "gender": "Male"
}
```

#### 💼 Balance Enquiry  
**POST** `/api/v1/user/balance-enquiry`  
```json
{
  "accountNumber": "1234567890"
}
```

#### 🙋 Name Enquiry  
**POST** `/api/v1/user/name-enquiry`  
```json
{
  "accountNumber": "1234567890"
}
```

---

### 💳 Transactions

#### ➕ Credit Account  
**POST** `/api/v1/transaction/credit`  
```json
{
  "accountNumber": "1234567890",
  "amount": 1000
}
```

#### ➖ Debit Account  
**POST** `/api/v1/transaction/debit`  
```json
{
  "accountNumber": "1234567890",
  "amount": 500
}
```

#### 🔁 Transfer Funds  
**POST** `/api/v1/transaction/transfer`  
```json
{
  "sourceAccountNumber": "1234567890",
  "destinationAccountNumber": "0987654321",
  "amount": 200
}
```

---

### 📄 Bank Statements

#### 🧾 Generate Statement  
**GET** `/api/v1/statement/{accountNumber}/{startDate}/{endDate}`  
Returns a list of transactions between `startDate` and `endDate`.

---
---

## 🗂️ Project Structure

```
src/
│
├── main/
│   ├── java/com/esmay/bankingService/
│   │   ├── controller     # REST Controllers
│   │   ├── dto            # Data Transfer Objects
│   │   ├── entity         # JPA Entity Models
│   │   ├── repository     # Spring Data Repositories
│   │   ├── service        # Business Logic
│   │   └── utils          # Helper/Utility Classes
│   └── resources/
│       └── application.properties  # Configuration
│
└── test/
    └── java/com/riduan/bms/  # Unit & Integration Tests
```

---

## 🧬 ER Diagram

![Banking ER Diagram](https://github.com/user-attachments/assets/db285fa5-01c4-4747-a90c-c75a89347560)

---

## ✅ Completed Features

- ✅ User account creation, balance inquiry, and name lookup  
- ✅ Credit, debit, and fund transfer operations  
- ✅ Statement generation by date range  
- ✅ Email notifications for all financial activities  

---

## 🚧 Roadmap

### 🔐 Authentication & Authorization *(Upcoming)*
- Implement security features such as **JWT-based authentication** to protect sensitive endpoints.

### 💻 Frontend Interface *(Planned)*
- Build a responsive **web or mobile frontend** to interact with the API in a user-friendly way.

---

## 🙏 Contributions & Feedback

Contributions are welcome! If you'd like to suggest improvements or report issues, feel free to open a pull request or submit an issue.

---

## 📬 Contact

Feel free to reach out with questions, feedback, or collaboration ideas.
