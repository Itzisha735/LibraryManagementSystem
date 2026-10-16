# University Library Management System

A Java-based web application designed to manage university library operations, including book management, user authentication, and role-based access control.

## Features

- User login and logout
- Secure password hashing
- Student and administrator roles
- Role-based permissions for book management
- View available books
- Add and delete books with administrator access
- Borrow and return books
- MySQL database integration
- Classic university-style user interface

## Technologies Used

- **Java** — application logic
- **Java Servlets** — request handling and authentication
- **JDBC** — database connectivity
- **MySQL** — database management
- **Apache Tomcat 11** — web application server
- **HTML and CSS** — user interface
- **Git and GitHub** — version control

## Project Structure

```text
LibraryManagementSystem/
├── css/
│   └── style.css
├── src/
│   └── com/library/
│       ├── dao/
│       ├── exceptions/
│       ├── interfaces/
│       ├── model/
│       └── servlet/
├── index.jsp
├── .gitignore
└── README.md
```

## Requirements

- JDK 26 or a compatible JDK
- Apache Tomcat 11
- MySQL 8
- MySQL Connector/J

## Database Configuration

1. Create the required MySQL database and tables.
2. Configure the database connection using the project's local `db.properties` file.
3. Keep database credentials private and do not commit them to GitHub.
4. Ensure MySQL is running before using database-dependent features.

## Running the Application

1. Install and configure the JDK, MySQL, and Apache Tomcat.
2. Configure the database connection.
3. Compile the Java source files and place the compiled classes in the application's `WEB-INF/classes` directory.
4. Place MySQL Connector/J in `WEB-INF/lib`.
5. Deploy the application as `LibraryManagementSystem` in Tomcat's `webapps` directory.
6. Start MySQL and Apache Tomcat.
7. Open the application in your browser:

   `http://localhost:8080/LibraryManagementSystem/`

## Security Notes

- Passwords are stored as hashes rather than plaintext.
- Database credentials and local test files should not be committed.
- Administrator functions are restricted by role.

## Project Purpose

This project demonstrates Java web development, database connectivity, authentication, object-oriented programming, and basic library management operations.

## Author

Student microproject — University Library Management System.
