# Supermarket Management System - Setup & Installation Guide

Welcome to the Supermarket Management System! This guide will walk you through setting up the project on a new computer. It covers Java installation, MySQL database setup, and compiling/running the application.

## 1. Prerequisites

Before starting, ensure you have the following installed on your new computer:

- **Java Development Kit (JDK 8 or higher)**: 
  - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or install via OpenJDK.
  - Verify installation by running `java -version` and `javac -version` in your terminal.
- **MySQL Server (5.7 or 8.0+)**:
  - Download from [MySQL Official Website](https://dev.mysql.com/downloads/installer/).
  - Make sure the MySQL service is running.

---

## 2. Database Installation & Configuration

The application requires a MySQL database named `supermarket_ms` to store users, categories, products, and bills.

### Step 2.1: Run the Database Script

Open your MySQL client (MySQL Workbench, DBeaver, or terminal) and run the following SQL:

```sql
CREATE DATABASE IF NOT EXISTS supermarket_ms DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE supermarket_ms;

-- User Table
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) DEFAULT NULL,
  `email` varchar(250) DEFAULT NULL,
  `mobileNumber` varchar(20) DEFAULT NULL,
  `address` varchar(500) DEFAULT NULL,
  `password` varchar(250) DEFAULT NULL,
  `securityQuestion` varchar(500) DEFAULT NULL,
  `answer` varchar(500) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Insert default admin (status is 'true' by default)
INSERT INTO `user` (`name`, `email`, `mobileNumber`, `address`, `password`, `securityQuestion`, `answer`, `status`) 
VALUES ('Admin', 'admin@gmail.com', '1234567890', 'Admin Address', 'admin', 'What is your favorite color?', 'Black', 'true');

-- Category Table
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Product Table
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Bill Table
CREATE TABLE `bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(200) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobileNumber` varchar(20) DEFAULT NULL,
  `date` varchar(50) DEFAULT NULL,
  `total` varchar(255) DEFAULT NULL,
  `createdBy` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

> **Note:** You can also find this script saved as `database.sql` in the root directory of this project.

### Step 2.2: Verify Default Admin User
The script automatically inserts the following admin account:
- Email: **admin@gmail.com**
- Password: **admin**

---

## 3. Environment & Application Setup

Now that the database is ready, you need to configure the Java application to connect to it.

### Step 3.1: Update Database Credentials
Open the project folder and navigate to `src/com/java1234/util/DbUtil.java`. 

Update the `dbUserName` and `dbPassword` variables to match your local MySQL credentials:

```java
public class DbUtil {
    private String dbUrl = "jdbc:mysql://localhost:3306/supermarket_ms?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
    
    // UPDATE THESE TO MATCH YOUR MYSQL SETUP:
    private String dbUserName = "root"; 
    private String dbPassword = "YOUR_MYSQL_PASSWORD_HERE"; 
    
    private String jdbcName = "com.mysql.cj.jdbc.Driver";
    // ...
}
```

> [!WARNING]
> If you are using MySQL 5.7 instead of 8.0+, you may need to change the `jdbcName` back to `"com.mysql.jdbc.Driver"`.

---

## 4. Compiling the Project

This project uses the `mysql-connector-java-8.0.30.jar` library located in the `lib` folder. You must include this jar in your classpath when compiling.

Open your terminal (or Command Prompt on Windows), navigate to the root directory of the project, and run the following command to compile all Java source files into the `bin` directory:

**For macOS / Linux:**
```bash
mkdir -p bin
javac -cp "lib/mysql-connector-java-8.0.30.jar:lib/itextpdf-5.5.13.3.jar" -d bin $(find src -name "*.java")
```

**For Windows (PowerShell):**
```powershell
mkdir bin
Get-ChildItem -Path src -Filter *.java -Recurse | ForEach-Object { javac -cp "lib/mysql-connector-java-8.0.30.jar;lib/itextpdf-5.5.13.3.jar" -d bin $_.FullName }
```

---

## 5. Running the Application

Once compiled, you can launch the application by running the `LogOnFrm` main class. Make sure to include both the `bin` folder and the MySQL connector library in your classpath.

**For macOS / Linux:**
```bash
java -cp "bin:lib/mysql-connector-java-8.0.30.jar:lib/itextpdf-5.5.13.3.jar" com.java1234.view.LoginPage
```

**For Windows:**
```powershell
java -cp "bin;lib/mysql-connector-java-8.0.30.jar;lib/itextpdf-5.5.13.3.jar" com.java1234.view.LoginPage
```

### Success!
The "Supermarket Management System" login screen should now appear. You can log in using the credentials you created earlier (Email: **admin@gmail.com**, Password: **admin**).
