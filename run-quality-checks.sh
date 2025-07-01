#!/bin/bash

# GitHub Actions Demo - Quality Checks Runner
# This script runs all code quality checks and tests

set -e  # Exit on any error

echo "🚀 Starting comprehensive quality checks for GitHub Actions Demo"
echo "================================================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if we're in the right directory
if [ ! -f "build.gradle" ]; then
    print_error "build.gradle not found. Please run this script from the project root."
    exit 1
fi

# Check Java version
print_status "Checking Java version..."
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" != "24" ]; then
    print_warning "Java 24 is recommended. Current version: $JAVA_VERSION"
else
    print_success "Java version: $JAVA_VERSION"
fi

# Clean previous builds
print_status "Cleaning previous builds..."
./gradlew clean

# 1. Code Formatting with Spotless
echo ""
print_status "1. Running Spotless (Code Formatting)..."
./gradlew spotlessCheck
print_success "Spotless check completed"

# 2. Checkstyle
echo ""
print_status "2. Running Checkstyle (Coding Standards)..."
./gradlew checkstyleMain checkstyleTest
print_success "Checkstyle completed"

# 3. PMD Static Analysis
echo ""
print_status "3. Running PMD (Static Code Analysis)..."
./gradlew pmdMain pmdTest
print_success "PMD completed"

# 4. SpotBugs
echo ""
print_status "4. Running SpotBugs (Bug Detection)..."
./gradlew spotbugsMain spotbugsTest
print_success "SpotBugs completed"

# 5. OWASP Dependency Check
echo ""
print_status "5. Running OWASP Dependency Check (Security)..."
./gradlew dependencyCheckAnalyze
print_success "OWASP Dependency Check completed"

# 6. Unit Tests with Coverage
echo ""
print_status "6. Running Unit Tests with JaCoCo Coverage..."
./gradlew test jacocoTestReport
print_success "Unit tests and coverage completed"

# 7. Mutation Testing with PIT
echo ""
print_status "7. Running PIT Mutation Testing..."
./gradlew pitest
print_success "Mutation testing completed"

# 8. Dependency Updates Check
echo ""
print_status "8. Checking for Dependency Updates..."
./gradlew dependencyUpdates
print_success "Dependency updates check completed"

# 9. Build Application
echo ""
print_status "9. Building Application..."
./gradlew build
print_success "Application build completed"

# 10. Integration Tests (if Docker is available)
echo ""
print_status "10. Checking Docker availability for integration tests..."
if command -v docker &> /dev/null && docker info &> /dev/null; then
    print_status "Docker is available. Running integration tests..."
    ./gradlew test --tests "*IntegrationTest"
    print_success "Integration tests completed"
else
    print_warning "Docker not available. Skipping integration tests."
fi

# Summary
echo ""
echo "================================================================"
echo "🎉 All quality checks completed successfully!"
echo "================================================================"

print_status "Reports generated in:"
echo "  - Test reports: build/reports/tests/"
echo "  - Coverage reports: build/reports/jacoco/"
echo "  - Mutation reports: build/reports/pitest/"
echo "  - SpotBugs reports: build/reports/spotbugs/"
echo "  - OWASP reports: build/reports/dependency-check/"
echo "  - PMD reports: build/reports/pmd/"
echo "  - Checkstyle reports: build/reports/checkstyle/"

print_status "Next steps:"
echo "  - Review the generated reports"
echo "  - Fix any issues found"
echo "  - Commit your changes"
echo "  - Push to trigger GitHub Actions CI/CD pipeline"

echo ""
print_success "Quality checks runner completed successfully!" 