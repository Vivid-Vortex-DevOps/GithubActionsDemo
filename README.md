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

This project implements a comprehensive set of code quality and testing tools. For detailed information about each tool, configuration, and usage, please refer to:

### 📋 **Detailed Documentation**
- **[Code Quality Tools Guide](checks.md)** - Complete list of all implemented tools with descriptions and configurations
- **[GitHub Actions Implementation](githubActionsChecks.md)** - CI/CD pipeline setup and GitHub Actions workflows

### 🚀 **Quick Start Commands**

```bash
# Run all quality checks at once
./run-quality-checks.sh          # Linux/Mac
run-quality-checks.bat           # Windows

# Individual tool commands
./gradlew spotlessCheck          # Code formatting
./gradlew checkstyleMain         # Coding standards
./gradlew pmdMain               # Static analysis
./gradlew spotbugsMain          # Bug detection
./gradlew test                  # All tests with coverage
./gradlew pitest                # Mutation testing
./gradlew dependencyCheckAnalyze # Security scan
./gradlew dependencyUpdates     # Check for updates
```

### 🔗 **External Tools**
- **[SonarCloud Dashboard](https://sonarcloud.io/summary/overall?id=Vivid-Vortex-DevOps_GithubActionsDemo&branch=master)** - Cloud-based code quality analysis
- **Swagger UI**: `http://localhost:8080/swagger-ui.html` - API documentation

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

### Running Tests & Quality Checks

For comprehensive testing and quality check commands, see the **[Code Quality Tools Guide](checks.md)**.

```bash
# Quick start - run all quality checks
./run-quality-checks.sh          # Linux/Mac
run-quality-checks.bat           # Windows

# Common commands
./gradlew test                   # Run all tests with coverage
./gradlew test --tests "*ControllerTest"  # Run specific test types
./gradlew pitest                 # Run mutation testing
./gradlew check                  # Run all quality checks
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

The project includes a comprehensive GitHub Actions workflow that automates all quality checks and deployment processes.

### 📋 **Pipeline Documentation**
- **[GitHub Actions Implementation Guide](githubActionsChecks.md)** - Detailed CI/CD setup and workflow configuration
- **Main Workflow**: `.github/workflows/ci-cd.yml` - Complete CI/CD pipeline
- **Quality Workflow**: `.github/workflows/quality.yml` - Dedicated quality checks

### 🚀 **Pipeline Stages**
1. **Code Quality Checks** - Spotless, Checkstyle, PMD, SpotBugs
2. **Security Scans** - OWASP dependency check, secrets detection
3. **Testing** - Unit tests, integration tests, mutation testing, coverage
4. **Build & Deploy** - Application build, Docker image, deployment

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
All tool configurations are located in the `config/` directory. For detailed configuration information, see the **[Code Quality Tools Guide](checks.md)**.

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