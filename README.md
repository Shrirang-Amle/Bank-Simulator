# 🚀 Full-Stack Web Application (React + Java + MySQL + SendGrid)

This project is a full-stack web application built using **React.js** for the frontend and **Java (Maven)** for the backend.  
It uses **MySQL** for data storage and integrates **SendGrid** for automated email notifications such as registration confirmations, feedback alerts, or general communication.

---

## 🧩 Project Overview

The system provides an interactive user interface built with React and a powerful backend developed in Java using Maven.  
The backend manages data operations, handles API requests, and integrates email functionality using the SendGrid API.  
The combination of these technologies provides a secure, scalable, and responsive full-stack solution.

---

## 🏗️ System Architecture

React Frontend (User Interface)
↓ (HTTP Requests)
Java Backend (Maven Project)
↓ (JDBC / DAO Layer)
MySQL Database
↑
SendGrid Email API (for outbound emails)
---

## ⚙️ Tech Stack

**Frontend:**  
- React.js  
- Axios (for API calls)  
- React Router DOM  
- TailwindCSS / Bootstrap (UI styling)

**Backend:**  
- Java 17+  
- Apache Maven  
- JDBC / DAO Architecture  
- JSON Parser (Gson or Jackson)  

**Database:**  
- MySQL 8.x  

**Email Service:**  
- SendGrid API  

---

## ✨ Features

- 🧑‍💻 User Registration and Login  
- 📨 Send Email Notifications using SendGrid  
- 🧾 CRUD Operations (Create, Read, Update, Delete)  
- 💾 Secure Data Management with MySQL  
- ⚙️ RESTful APIs for Frontend Communication  
- 💻 Responsive and Interactive React Frontend  

---

## 🧰 Prerequisites

Make sure you have the following installed:

- Node.js (v18+)
- npm or yarn  
- Java JDK 17 or later  
- Apache Maven (v3.8+)  
- MySQL Server  
- SendGrid Account (for email API key)

---

## 🗄️ Database Setup

1. Create a new database in MySQL:
   ```sql
   CREATE DATABASE project_db;
Update your database configuration (in dbconfig.properties or constants file):

properties
db.url=jdbc:mysql://localhost:3306/project_db
db.username=root
db.password=yourpassword
db.driver=com.mysql.cj.jdbc.Driver
📧 SendGrid Email Setup
Create a free account on SendGrid.

Generate an API key with full access.

Add the key to your system environment variables or configuration file:

properties
sendgrid.api.key=YOUR_SENDGRID_API_KEY
sendgrid.from.email=youremail@example.com
Sample email sending code in Java:

import com.sendgrid.*;

public class EmailService {
    public static void sendMail(String to, String subject, String messageText) throws IOException {
        Email from = new Email("youremail@example.com");
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", messageText);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        sg.api(request);
    }
}
⚛️ Frontend Setup (React)
Navigate to your frontend folder and install dependencies:

cd frontend
npm install
npm start
The app runs by default on http://localhost:3000.

Create a .env file in the frontend/ folder:

REACT_APP_API_URL=http://localhost:8080
☕ Backend Setup (Java + Maven)
Navigate to your backend folder:

cd backend
Build and run the project:

mvn clean install
java -jar target/projectname.jar
If running from an IDE (like IntelliJ or Eclipse), simply run Main.java.
