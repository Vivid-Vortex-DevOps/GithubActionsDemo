# GitHub Actions Demo - Spring Boot with Comprehensive Code Quality Tools

This project demonstrates a Spring Boot application with a comprehensive set of code quality and testing tools integrated into a CI/CD pipeline.

## 🚀 Features

- **Spring Boot 3.5.3** with Java 24
- **RESTful API** with OpenAPI documentation
- **Comprehensive testing** with unit, integration, and mutation testing
- **Code quality tools** for formatting, static analysis, and security
- **CI/CD pipeline** with GitHub Actions
- **Containerization** with Docker
- **Architecture enforcement** with ArchUnit

## 🛠️ Code Quality Tools Implemented

### 1. **Spotless** - Code Formatting
- **Purpose**: Ensures consistent code formatting across the project
- **Configuration**: Uses Google Java Format
- **Usage**: 
  ```bash
  ./gradlew spotlessApply    # Format code
  ./gradlew spotlessCheck    # Check formatting
  ```

### 2. **Checkstyle** - Coding Standards
- **Purpose**: Enforces Java coding standards and conventions
- **Configuration**: `config/checkstyle/checkstyle.xml`
- **Usage**:
  ```bash
  ./gradlew checkstyleMain checkstyleTest
  ```

### 3. **PMD** - Static Code Analysis
- **Purpose**: Detects code issues like empty catch blocks, hardcoded literals
- **Configuration**: `config/pmd/ruleset.xml`
- **Usage**:
  ```bash
  ./gradlew pmdMain pmdTest
  ```

### 4. **SpotBugs** - Bug Detection
- **Purpose**: Analyzes bytecode to detect potential bugs
- **Configuration**: `config/spotbugs/exclude.xml`
- **Usage**:
  ```bash
  ./gradlew spotbugsMain spotbugsTest
  ```

### 5. **PIT (Pitest)** - Mutation Testing
- **Purpose**: Tests the quality of unit tests by making code changes
- **Configuration**: Set in `build.gradle`
- **Usage**:
  ```bash
  ./gradlew pitest
  ```

### 6. **OWASP Dependency Check** - Security Vulnerabilities
- **Purpose**: Scans dependencies for known security vulnerabilities
- **Configuration**: `config/dependency-check/suppressions.xml`
- **Usage**:
  ```bash
  ./gradlew dependencyCheckAnalyze
  ```

### 7. **JaCoCo** - Code Coverage
- **Purpose**: Measures test coverage with thresholds
- **Configuration**: Set in `build.gradle`
- **Usage**:
  ```bash
  ./gradlew test jacocoTestReport
  ```

### 8. **ArchUnit** - Architecture Testing
- **Purpose**: Enforces architectural rules and layering
- **Usage**: Run with tests
- **Examples**: See `src/test/java/.../architecture/ArchitectureTest.java`

### 9. **AssertJ** - Fluent Assertions
- **Purpose**: Provides readable and expressive test assertions
- **Usage**: Used in all test classes

### 10. **Testcontainers** - Integration Testing
- **Purpose**: Run containerized databases during tests
- **Usage**: See `src/test/java/.../integration/UserIntegrationTest.java`

### 11. **Gradle Versions Plugin** - Dependency Updates
- **Purpose**: Detects outdated dependencies
- **Usage**:
  ```bash
  ./gradlew dependencyUpdates
  ```

### 12. **Springdoc OpenAPI** - API Documentation
- **Purpose**: Generates OpenAPI 3 documentation
- **Access**: `http://localhost:8080/swagger-ui.html`

### 13. **JSON Schema Validation** - API Contract Testing
- **Purpose**: Validates JSON payloads against schemas
- **Usage**: See `src/test/java/.../validation/JsonSchemaValidationTest.java`

### 14. **SonarCloud** - Code Quality Analysis
- **Purpose**: Cloud-based code quality analysis with dashboards and quality gates
- **Access**: [SonarCloud Dashboard](https://sonarcloud.io/summary/overall?id=Vivid-Vortex-DevOps_GithubActionsDemo&branch=master)
- **Features**: 
  - Real-time code analysis
  - Quality gates and metrics
  - Pull request decoration
  - Multi-language support (Java, JavaScript, TypeScript, C#, Python, etc.)

## 🏗️ Project Structure

```
src/
├── main/java/com/demo/actions/GithubActionsDemo/
│   ├── controller/          # REST controllers
│   ├── service/            # Business logic
│   ├── dto/                # Data Transfer Objects
│   └── exception/          # Custom exceptions
├── test/java/.../
│   ├── controller/         # Controller tests
│   ├── service/           # Service tests
│   ├── integration/       # Integration tests
│   ├── architecture/      # ArchUnit tests
│   └── validation/        # JSON Schema tests
└── test/resources/
    └── schemas/           # JSON schemas
```

## 🚀 Getting Started

### Prerequisites
- Java 24
- Docker (for integration tests)
- Git

### Running the Application

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd GithubActionsDemo
   ```

2. **Run the application**:
   ```bash
   ./gradlew bootRun
   ```

3. **Access the API**:
   - Application: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - API Endpoints: `http://localhost:8080/api/users`

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test types
./gradlew test --tests "*ControllerTest"
./gradlew test --tests "*ServiceTest"
./gradlew test --tests "*IntegrationTest"

# Run with coverage
./gradlew test jacocoTestReport

# Run mutation testing
./gradlew pitest
```

### Code Quality Checks

```bash
# Run all quality checks
./gradlew check

# Individual checks
./gradlew spotlessCheck
./gradlew checkstyleMain checkstyleTest
./gradlew pmdMain pmdTest
./gradlew spotbugsMain spotbugsTest
./gradlew dependencyCheckAnalyze
```

### Building and Running with Docker

```bash
# Build the application
./gradlew build

# Build Docker image
docker build -t github-actions-demo .

# Run with Docker
docker run -p 8080:8080 github-actions-demo
```

## 🔄 CI/CD Pipeline

The project includes a comprehensive GitHub Actions workflow (`.github/workflows/ci-cd.yml`) that:

1. **Code Quality Checks**:
   - Spotless formatting check
   - Checkstyle validation
   - PMD static analysis
   - SpotBugs bug detection

2. **Security Checks**:
   - OWASP dependency vulnerability scan
   - Secret detection with TruffleHog

3. **Testing**:
   - Unit and integration tests
   - PIT mutation testing
   - JaCoCo coverage reporting

4. **Build and Deploy**:
   - Application build
   - Docker image creation
   - Deployment to staging/production

## 📊 API Endpoints

### User Management

- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Example Usage

```bash
# Create a user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "age": 30,
    "phoneNumber": "+1-555-123-4567"
  }'

# Get all users
curl http://localhost:8080/api/users

# Get user by ID
curl http://localhost:8080/api/users/1
```

## 🔧 Configuration

### Application Properties
- `src/main/resources/application.properties` - Main configuration
- `src/test/resources/application-test.properties` - Test configuration

### Tool Configurations
- `config/checkstyle/checkstyle.xml` - Checkstyle rules
- `config/pmd/ruleset.xml` - PMD rules
- `config/spotbugs/exclude.xml` - SpotBugs exclusions
- `config/dependency-check/suppressions.xml` - OWASP suppressions

## 📈 Quality Metrics

The project enforces the following quality thresholds:

- **Code Coverage**: Minimum 80% line coverage, 70% branch coverage
- **Mutation Testing**: Minimum 80% mutation coverage
- **Security**: CVSS score below 7 for dependencies
- **Code Style**: Google Java Format compliance

## 🐛 Troubleshooting

### Common Issues

1. **Java Version**: Ensure you're using Java 24
   ```bash
   java -version
   ```

2. **Docker Issues**: For integration tests, ensure Docker is running
   ```bash
   docker --version
   ```

3. **Gradle Issues**: Clean and rebuild
   ```bash
   ./gradlew clean build
   ```

### Getting Help

- Check the test reports in `build/reports/`
- Review the GitHub Actions logs
- Consult the tool-specific documentation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Ensure all quality checks pass
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- All the open-source tool maintainers
- The Java community for best practices and tools 