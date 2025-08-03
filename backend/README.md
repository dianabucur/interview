# Java Spring Boot API Coding Exercise

## Steps to get started:

#### Prerequisites
- Maven
- Java 1.8 (or higher, update version in pom.xml if needed)

#### Fork the repository and clone it locally
- https://github.com/Tekmetric/interview.git

#### Import project into IDE
- Project root is located in `backend` folder

#### Build and run your app
- `mvn package && java -jar target/interview-1.0-SNAPSHOT.jar`

#### Test that your app is running
- `curl -X GET   http://localhost:8080/api/welcome`

#### After finishing the goals listed below create a PR

### Goals
1. Design a CRUD API with data store using Spring Boot and in memory H2 database (pre-configured, see below)
2. API should include one object with create, read, update, and delete operations. Read should include fetching a single item and list of items.
3. Provide SQL create scripts for your object(s) in resources/data.sql
4. Demo API functionality using API client tool

### Considerations
This is an open ended exercise for you to showcase what you know! We encourage you to think about best practices for structuring your code and handling different scenarios. Feel free to include additional improvements that you believe are important.

#### H2 Configuration
- Console: http://localhost:8080/h2-console 
- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: password

### Submitting your coding exercise
Once you have finished the coding exercise please create a PR into Tekmetric/interview

# Hiking APP

A Spring Boot application for tracking and managing hikes. It can be used as a hiking journal and search engine for trails.

### Prerequisites
- Maven 3.8+
- Java 21+
- Docker
- Working Internet Connection

### Technology Stack
- Java 21
- Spring Boot 3
- H2 in-memory database for quick dev setup
- Redis for caching
- Swagger (OpenAPI 3) for interactive API documentation
- Prometheus metrics export
- Grafana dashboards for monitoring

### Build & Run

- `mvn clean build`
- `docker-compose up --build`

### Accessibility
- Data visualization (H2 Console): http://localhost:8080/h2-console
  - username: sa
  - pass: password
- Documentation (OpenAPI Swagger): http://localhost:8080/swagger-ui/index.html
- Monitoring (Grafana): http://localhost:3000/
  - username: admin
  - pass: password