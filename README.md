# Product-Management-System-Java
 A Java-based Product Management System designed to streamline the management of product categories, stock levels, and product entries. This application is developed using NetBeans IDE and MySQL for the database. The project includes comprehensive functionality for adding, updating, and managing products and their categories, with an easy-to-navigate user interface.




Here's the updated README.md with the screenshot details included:

---

# Product-Management-System-Java

## Overview

A Java-based Product Management System for managing product categories, stock levels, and entries. Developed in NetBeans IDE with MySQL, it offers comprehensive functionality for adding, updating, and managing products, featuring an intuitive user interface for streamlined inventory management.

## Features

- **Product Categories Management**: Manage product categories with add, update, and delete functionalities.
- **Product Stock Management**: Track and update stock levels, including alerting on low stock.
- **Product Entry Management**: Handle detailed product information such as pricing and categorization.
- **Product Sale/Selling**: Efficiently manage product sales operations.

## Plan Details

### User Interface Components

1. **Product Entry**
   - Product Id (PK)
   - Product Categories (FK)
   - Product Name
   - Unit Purchasing Price
   - Unit Selling Price

2. **Product Categories**
   - Product Categories Id (PK)
   - Product Categories (String)

3. **Product Stock**
   - Product Id (PK)
   - Product Category
   - Product Name
   - Already in Stock
   - Now Loaded Stock
   - Total Stock
   - Alert Level
   - Total Cost for the New Stock
   - Total Money If All New Stock Over
   - Total Income If All Stock Are Over

4. **Product Sale/Selling**
   - Manage and track product sales.

### Database Structure

1. **Product Categories**
   - Product Categories Id (PK)
   - Product Categories (String)

2. **Product Entry**
   - Product Id (PK)
   - Product Categories (FK)
   - Product Name
   - Unit Purchasing Price
   - Unit Selling Price

3. **Product Stock**
   - Product Id (PK)
   - Stock (Int)
   - Alert Level (Int)

## Installation Instructions

### Prerequisites

- **Java Development Kit (JDK) 8+**
- **NetBeans IDE 8.2 or later**
- **MySQL Server**

### Steps

1. **Clone the Repository**:
   ```sh
   git clone https://github.com/yourusername/Product-Management-System-Java.git
   ```
   
2. **Open in NetBeans**:
   - Launch NetBeans IDE.
   - Go to `File > Open Project`.
   - Navigate to the cloned directory `ProductManagmentSystem` and open the project.

3. **Database Setup**:
   - Import the database dump file located in `DatabaseDumpFile` into your MySQL server.
   - The database can be imported using the following MySQL command:
     ```sh
     mysql -u root -p as1234 < path_to_database_dump.sql
     ```
   - Update the database connection details if needed by modifying the `db` class inside the `Database` package within the project. The current configuration is:
     - Host: `localhost`
     - Username: `root`
     - Password: `1234`
     - Database Name: `product_management_system`

4. **Run the Project**:
   - After configuring the database, right-click on the project in NetBeans and select `Run`.

## Project Structure

- **DatabaseDumpFile**: Contains the MySQL database dump.
- **ProductManagmentSystem**: Main project folder containing all the Java source files and configurations.
- **Screenshots**: Visual references of the application interface.
   - `Product Categories.jpeg`: Screenshot of the Product Categories interface.
- **Documentation**: Contains the project plan and additional documents.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

This README now includes details about the screenshot located in the 'Screenshot' folder.
## Screenshots

- **Product Entry**
  
  ![Product Entry](Screenshot/product_entry.jpeg)
  
- **Product Categories**
  
  ![Product Categories](Screenshot/product_categories.jpeg)
  
- **Product Stock**
  
  ![Product Stock](Screenshot/product_stock.jpeg)


