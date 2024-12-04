# VRV Security Assignment API

## Overview
The VRV Security Assignment API enables the registration, login, and secure access of users, using JWT authentication. It integrates an H2 database for storing user and role data, with endpoints for registering and logging in users, as well as accessing secure endpoints.

## Features
1. User Registration (POST /api/auth/register):
	a. Creates a user in the database.
	b. Positive response confirming successful registration.

2. User Login (POST /api/auth/login):
	a. Validates user credentials and returns a JWT token for further authentication.

3. Access Secure Endpoint (GET /api/auth/secure-endpoint):
	a. The secure endpoint can be accessed by providing a valid JWT token in the Authorization header.

4. Database Interaction:
	a. The database contains tables USER, USER_ROLE, and USER_ROLES.
	b. You can query, view, and validate relationships and indexes in the database via H2 console.


## Folder Structure
vrv-security-assignment/             # Root directory of the project
├── .mvn/                           # Maven wrapper files
│   └── wrapper/                    # Contains Maven wrapper files to run Maven commands without installing Maven
├── .vscode/                         # VS Code settings and configurations
│   └── settings/                    # VS Code workspace settings (e.g., recommended extensions)
├── data/                            # Contains data files
│   └── testdb.mv                    # H2 database file (used for persistent storage)
├── src/                             # Source code files
│   ├── main/                        # Main application files
│   │   ├── java/                    # Java source files
│   │   │   └── com/dishakaw/assignment/    # The base package for your application
│   │   │       ├── AssignmentApplication.java    # Main entry point for the Spring Boot application
│   │   │       ├── controller/              # Controllers for handling HTTP requests
│   │   │       │   ├── AuthController.java    # Handles authentication requests (e.g., login, register)
│   │   │       │   ├── HomeController.java   # Handles home endpoint requests
│   │   │       ├── model/                   # Java classes representing the data models
│   │   │       │   ├── User.java            # Represents a user entity
│   │   │       │   ├── Role.java            # Represents a role for the user
│   │   │       │   ├── UserRole.java        # Represents user-role association
│   │   │       ├── repository/              # Repositories for database interaction
│   │   │       │   ├── AssignmentUserRepository.java  # Custom repository for Assignment user
│   │   │       │   ├── UserRepository.java  # Repository for User CRUD operations
│   │   │       ├── security/                # Security-related classes (authentication, authorization)
│   │   │       │   ├── JwtAuthenticationFilter.java  # JWT authentication filter
│   │   │       │   ├── SecurityConfig.java   # Security configuration for Spring Security
│   │   │       ├── service/                 # Business logic and service classes
│   │   │       │   ├── AuthService.java     # Handles user authentication and registration logic
│   │   │       ├── utils/                   # Utility classes (e.g., for JWT token operations)
│   │   │       │   ├── JwtUtil.java         # Handles JWT token creation and validation
│   │   └── resources/                      # Application resources (e.g., configuration files)
│   │       ├── static                      # Static resources like CSS, JS, images (if any)
│   │       ├── templates                   # Thymeleaf templates (if any)
│   │       ├── application.properties      # Spring Boot application configuration (database, server, etc.)
│   └── test/                             # Test files
│       ├── java/                         # Java test classes
│       │   ├── com/dishakaw/assignment/tests/  # Test package
│       │   │   ├── AuthServiceTest.java    # Tests for the AuthService
│       │   │   ├── AuthControllerTest.java # Tests for the AuthController
│       └── resources/                     # Resources for testing (if any)
├── target/                            # Compiled output files (JAR, classes, etc.)
├── .gitattributes                     # Git attributes for configuring Git behavior
├── .gitignore                         # Git ignore file to specify files/folders to exclude
├── HELP                                # Help file (if any)
├── mvnw                                # Maven wrapper shell script
├── mvnw.cmd                            # Maven wrapper Windows command script
├── pom.xml                             # Maven project configuration file
└── README.md                           # Project documentation (this file)


## Setup Instructions
1. Clone the Repository:
	git clone <repository_url>
	cd vrv-security-assignment
	
2. Build the Project:
	mvn clean install

3. Run the Application: After building the project, you can run it using the following command:
	java -jar target/assignment-0.0.1-SNAPSHOT.jar
	The application will start on http://localhost:8080.


## H2 Database Access
	1. H2 Console: After starting the application, access the H2 Console:
	http://localhost:8080/h2-console
		a. JDBC URL: jdbc:h2:mem:testdb
		b. Username: sa
		c. Password: Leave blank

	2. Database Queries: 
		a. To view the data in the USER table: SELECT * FROM USER;
		b.To view the data in the USER_ROLE table: SELECT * FROM USER_ROLE;
		c. To view the data in the USER_ROLES table: SELECT * FROM USER_ROLES;

	3.Validate Foreign Key Constraints:
		a. Ensure there are no orphaned records in USER_ROLES: SELECT * FROM USER_ROLES WHERE USER_ID NOT IN (SELECT ID FROM USER);

	4. Checking for Duplicate Records:
		a. Check for duplicate users: SELECT username, COUNT(*) 
			FROM USER 
			GROUP BY username 
			HAVING COUNT(*) > 1;

	5.  Check Indexes on Tables:
		a.View indexes on USER and USER_ROLES tables:
		SELECT * FROM INFORMATION_SCHEMA.INDEXES WHERE TABLE_NAME = 'USER';
		SELECT * FROM INFORMATION_SCHEMA.INDEXES WHERE TABLE_NAME = 'USER_ROLES';


## API Endpoints:
	1. User Registration:
		a.POST /api/auth/register:
		b.Request Body: {
    "username": "testuser",
    "password": "password",
    "roles": ["ROLE_USER"]}

		c.Response: {
    "message": "User registered successfully"}

	2. User Login:
		a.POST /api/auth/login:
		b.Request Body: {
    "username": "testuser",
    "password": "password"}


		c.Response: {
    "token": "JWT_TOKEN"}



	3. Access Secure Endpoint:
		a.GET /api/auth/secure-endpoint:
		b.Request Send a valid JWT token in the Authorization header as Bearer <token>.

		c.Response: {
    "message": "You have access to this secure endpoint!"}


## Important Notes
1. H2 Console:
	a.The H2 database used in this application runs in-memory by default, meaning data will be lost after a server restart.
	b.If you need persistent storage, consider changing the configuration to use a file-based database.

2. JWT Expiry:
	a. JWT tokens may have an expiry time. If you receive a 401 Unauthorized error, try logging in again to get a new token.

3. Database Constraints:
	a.JWT tokens may have an expiry time. If you receive a 401 Unauthorized error, try logging in again to get a new token.

4.Testing:
	a.The API can be tested using tools like Postman or curl by sending HTTP requests to the respective endpoints.

5. Ensure Java 11:
	a.Ensure that you have Java 11 installed on your machine as the project is built with Java 11.


## Future Enhancements
1. Role-based Authorization:
	Implement more granular role-based access control.

2. Multitenancy:
Allow different users to access their own databases and information.

3. Logging and Monitoring:
Add enhanced logging and monitoring for production environments.

4. CI/CD Integration:
Set up automated deployments using Jenkins, GitLab CI, or GitHub Actions.



## Author
Developed by Disha Kaw.
For queries, feel free to reach out at:
Email: dishakaw2002@gmail.com
