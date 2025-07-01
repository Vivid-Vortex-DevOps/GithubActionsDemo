@echo off
REM GitHub Actions Demo - Quality Checks Runner (Windows)
REM This script runs all code quality checks and tests

echo 🚀 Starting comprehensive quality checks for GitHub Actions Demo
echo ================================================================

REM Check if we're in the right directory
if not exist "build.gradle" (
    echo [ERROR] build.gradle not found. Please run this script from the project root.
    exit /b 1
)

REM Check Java version
echo [INFO] Checking Java version...
java -version 2>&1 | findstr "version"
if %errorlevel% neq 0 (
    echo [ERROR] Java not found. Please install Java 24.
    exit /b 1
)

REM Clean previous builds
echo [INFO] Cleaning previous builds...
call gradlew clean

REM 1. Code Formatting with Spotless
echo.
echo [INFO] 1. Running Spotless (Code Formatting)...
call gradlew spotlessCheck
if %errorlevel% neq 0 (
    echo [ERROR] Spotless check failed
    exit /b 1
)
echo [SUCCESS] Spotless check completed

REM 2. Checkstyle
echo.
echo [INFO] 2. Running Checkstyle (Coding Standards)...
call gradlew checkstyleMain checkstyleTest
if %errorlevel% neq 0 (
    echo [ERROR] Checkstyle failed
    exit /b 1
)
echo [SUCCESS] Checkstyle completed

REM 3. PMD Static Analysis
echo.
echo [INFO] 3. Running PMD (Static Code Analysis)...
call gradlew pmdMain pmdTest
if %errorlevel% neq 0 (
    echo [ERROR] PMD failed
    exit /b 1
)
echo [SUCCESS] PMD completed

REM 4. SpotBugs
echo.
echo [INFO] 4. Running SpotBugs (Bug Detection)...
call gradlew spotbugsMain spotbugsTest
if %errorlevel% neq 0 (
    echo [ERROR] SpotBugs failed
    exit /b 1
)
echo [SUCCESS] SpotBugs completed

REM 5. OWASP Dependency Check
echo.
echo [INFO] 5. Running OWASP Dependency Check (Security)...
call gradlew dependencyCheckAnalyze
if %errorlevel% neq 0 (
    echo [ERROR] OWASP Dependency Check failed
    exit /b 1
)
echo [SUCCESS] OWASP Dependency Check completed

REM 6. Unit Tests with Coverage
echo.
echo [INFO] 6. Running Unit Tests with JaCoCo Coverage...
call gradlew test jacocoTestReport
if %errorlevel% neq 0 (
    echo [ERROR] Unit tests failed
    exit /b 1
)
echo [SUCCESS] Unit tests and coverage completed

REM 7. Mutation Testing with PIT
echo.
echo [INFO] 7. Running PIT Mutation Testing...
call gradlew pitest
if %errorlevel% neq 0 (
    echo [ERROR] Mutation testing failed
    exit /b 1
)
echo [SUCCESS] Mutation testing completed

REM 8. Dependency Updates Check
echo.
echo [INFO] 8. Checking for Dependency Updates...
call gradlew dependencyUpdates
if %errorlevel% neq 0 (
    echo [ERROR] Dependency updates check failed
    exit /b 1
)
echo [SUCCESS] Dependency updates check completed

REM 9. Build Application
echo.
echo [INFO] 9. Building Application...
call gradlew build
if %errorlevel% neq 0 (
    echo [ERROR] Application build failed
    exit /b 1
)
echo [SUCCESS] Application build completed

REM 10. Integration Tests (if Docker is available)
echo.
echo [INFO] 10. Checking Docker availability for integration tests...
docker --version >nul 2>&1
if %errorlevel% equ 0 (
    echo [INFO] Docker is available. Running integration tests...
    call gradlew test --tests "*IntegrationTest"
    if %errorlevel% neq 0 (
        echo [ERROR] Integration tests failed
        exit /b 1
    )
    echo [SUCCESS] Integration tests completed
) else (
    echo [WARNING] Docker not available. Skipping integration tests.
)

REM Summary
echo.
echo ================================================================
echo 🎉 All quality checks completed successfully!
echo ================================================================

echo [INFO] Reports generated in:
echo   - Test reports: build\reports\tests\
echo   - Coverage reports: build\reports\jacoco\
echo   - Mutation reports: build\reports\pitest\
echo   - SpotBugs reports: build\reports\spotbugs\
echo   - OWASP reports: build\reports\dependency-check\
echo   - PMD reports: build\reports\pmd\
echo   - Checkstyle reports: build\reports\checkstyle\

echo [INFO] Next steps:
echo   - Review the generated reports
echo   - Fix any issues found
echo   - Commit your changes
echo   - Push to trigger GitHub Actions CI/CD pipeline

echo.
echo [SUCCESS] Quality checks runner completed successfully!
pause 