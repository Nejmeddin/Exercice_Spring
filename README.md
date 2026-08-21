# Exercice_Spring

A comprehensive Spring MVC web application demonstrating modern Spring Framework practices with JPA, Hibernate, Spring Security, and Thymeleaf templating.

## 📋 Project Overview

**Exercice_Spring** is a full-stack web application built with Spring Framework that provides a complete CRUD (Create, Read, Update, Delete) system for managing persons. The application includes user authentication, role-based access control, and advanced search capabilities with specification-based filtering.

### Key Features

- ✅ **User Authentication & Authorization** - Spring Security integration with custom user details service
- ✅ **Person Management** - Complete CRUD operations for managing person records
- ✅ **Advanced Search** - Dynamic filtering by multiple criteria (name, first name, civility)
- ✅ **Role-Based Access Control** - Different user roles with appropriate permissions
- ✅ **Modern UI** - Responsive interface using Bootstrap 5 and Thymeleaf templates
- ✅ **Data Persistence** - Hibernate ORM with MySQL database
- ✅ **Input Validation** - Jakarta Bean Validation with custom error handling

## 🛠 Tech Stack

| Component | Version | Purpose |
|-----------|---------|---------|
| **Java** | 21 | Programming language |
| **Spring Framework** | 6.2.3 | Core framework |
| **Spring Security** | 6.4.2 | Authentication & Authorization |
| **Spring Data JPA** | 3.4.3 | Data access layer |
| **Hibernate** | 7.0.0.Final | ORM |
| **Thymeleaf** | 3.1.3 | Server-side templating |
| **Bootstrap** | 5.3.3 | CSS framework |
| **MySQL** | 8.3.0 | Database |
| **Jakarta EE** | 10 (servlet 6.1, JPA 3.2) | Specifications |
| **Lombok** | 1.18.34 | Reduce boilerplate code |
| **Maven** | Latest | Build tool |

## 📁 Project Structure

```
Exercice_Spring/
├── src/
│   ├── main/
│   │   ├── java/tn/enis/app/
│   │   │   ├── controller/          # Spring Controllers
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── PersonController.java
│   │   │   │   ├── UserController.java
│   │   │   │   └── LoginController.java
│   │   │   ├── service/             # Business logic layer
│   │   │   │   └── PersonService.java
│   │   │   ├── security/            # Security configuration
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   ├── entities/            # JPA Entities
│   │   │   ├── repository/          # Spring Data JPA Repositories
│   │   │   └── config/              # Configuration classes
│   │   └── resources/
│   │       └── templates/           # Thymeleaf HTML templates
│   └── test/                        # Unit & Integration tests
├── pom.xml                          # Maven configuration
└── README.md                        # This file
```

## 🚀 Getting Started

### Prerequisites

- **Java 21** or higher
- **Maven 3.8+**
- **MySQL 8.0+**
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Nejmeddin/Exercice_Spring.git
   cd Exercice_Spring
   ```

2. **Configure the database**
   - Create a MySQL database
   - Update connection properties in `application.properties` or `application.yml`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/exercice_spring
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   Or deploy the generated `.war` file to a servlet container (Tomcat, Jetty)

5. **Access the application**
   - Open your browser and navigate to: `http://localhost:8080/`

## 📖 Core Components

### Controllers

- **HomeController** - Handles home page and access-denied routes
- **PersonController** - Manages person CRUD operations and search functionality
- **UserController** - Displays user list
- **LoginController** - Handles login page routing

### Services

- **PersonService** - Implements business logic for person operations
  - Search with dynamic specifications
  - CRUD operations
  - Data validation

### Security

- **CustomUserDetailsService** - Custom implementation loading user details from the database
- Spring Security configuration with role-based authorization
- Login form-based authentication

## 🔐 User Roles

The application supports different user roles:
- **ADMIN** - Full access to all features
- **USER** - Limited access to person records
- **GUEST** - Read-only access

## 🔍 Key Features in Detail

### Person Search
Search persons by:
- **Nom** (Last Name)
- **Prénom** (First Name)
- **Civilité** (Civility - Mr, Ms, Mrs, etc.)

Supports partial matching and multiple criteria combinations.

### Person Management
- **View All** - List all persons in the system
- **View Details** - See detailed information for a specific person
- **Add New** - Create new person record with validation
- **Update** - Modify existing person information
- **Delete** - Remove person records

### Data Validation
- Jakarta Bean Validation annotations
- Custom error messages
- Form-level validation feedback

## 🧪 Testing

Run tests using Maven:
```bash
mvn test
```

## 📝 Dependencies

Key Maven dependencies:
- Spring Framework (MVC, ORM, TX)
- Spring Data JPA
- Spring Security
- Hibernate & Jakarta Persistence API
- Thymeleaf
- WebJars (Bootstrap, Bootstrap Icons)
- Lombok
- MySQL JDBC Driver
- JUnit 5

For complete dependencies, refer to `pom.xml`

## 🐛 Troubleshooting

### Database Connection Issues
- Verify MySQL server is running
- Check connection credentials in configuration files
- Ensure the database exists

### Authentication Issues
- Clear browser cache and cookies
- Verify user credentials in the database
- Check Spring Security configuration

### Build Errors
- Ensure Java 21 is installed: `java -version`
- Update Maven: `mvn -v`
- Clean build: `mvn clean install -U`

## 📌 Version History

- **v1.0-SNAPSHOT** - Initial release with core features

## 📄 License

This project is open source and available under the MIT License.

## 👤 Author

**Nejmeddin** - [GitHub Profile](https://github.com/Nejmeddin)

## 🤝 Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

## 📮 Support

For questions or issues, please open an issue on the GitHub repository.

---

**Last Updated**: March 2026
