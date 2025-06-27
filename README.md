# EasyShop API

## Description of the Project

This is a Java backend API powers e-commerce platform designed to handle product listings, user authentication, shopping cart functionality, and order management. The application is built using Spring Boot and connects to a MySQL database. It includes everything from user registration and login via tokens, to role-based permissions, product filtering, and shopping cart persistence. 


## User Stories

- As a registered user, I want to log in and stay logged in, so I can manage my cart and orders securely.
- As a user, I want to view and search for products by category, color, or price range so I can find what I’m looking for.
- As an admin, I want to add, update, or delete categories and products so I can manage the store’s inventory.
- As a user, I want my shopping cart to save items even after logging out, so I can pick up where I left off.
- As a user, I want to convert my cart into an order easily, so I can complete purchases without confusion.
- As a user, I want to filter products in the existing filters (price, color, category).

## Setup

### Prerequisites

- IntelliJ IDEA: Download it [here](https://www.jetbrains.com/idea/download/)
- Java SDK 17 or higher
- MySQL Server and MySQL Workbench
- Postman (optional, for testing endpoints)

### Running the Application in IntelliJ

1. Open IntelliJ IDEA.
2. Clone or download the GitHub repo to your local machine.
3. Open the project folder from IntelliJ.
4. Wait for dependencies to load and the project to index.
5. Ensure the MySQL database is set up by running `create_database.sql`.
6. Locate your main class containing `public static void main(String[] args)` and run it.
7. Use Postman or the frontend site provided to start interacting with the API.

## Technologies Used

- Java 17
- Spring Boot
- MySQL
- JDBC
- Apache Maven
- Spring Security (JWT authentication)
- Postman (API testing)

## Demo

![Ap![Screenshot 2025-06-25 201929.png](../../../../../Pictures/Screenshots/Screenshot%202025-06-25%20201929.png)plication Screenshot](path/to/your/screenshot.png)

## Future Work

- Add checkout confirmation with email notifications
- Improve product deduplication and editing safeguards
- Build out a full admin dashboard UI
- Add product rating and review system

## Resources

- https://spring.io/projects/spring-boot
- https://www.baeldung.com/spring-security-oauth-jwt
- https://dev.mysql.com/doc/connector-j/8.0/en/
- https://github.com/RayMaroun/yearup-spring-section-10-2025/tree/master/pluralsight/java-development/workbook-9/apireview

## Thanks

- Thank you to Ray for support, code reviews, and explaining the real-world implications of backend security and data integrity.
- Shoutout to the YearUp crew and all the late-night Postman test warriors.
