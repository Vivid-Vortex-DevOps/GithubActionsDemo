### ✅ **Recommended GitHub Actions for Code Quality & Standards**

---

#### 🔹 1. **Spotless Check (Code Formatting)**

Automatically verifies that your code follows formatting rules.

> **Usage:**

```yaml
- name: Run Spotless Check
  run: ./gradlew spotlessCheck
```

---

#### 🔹 2. **Checkstyle Linting**

Validates code against your Checkstyle configuration.

> **Usage:**

```yaml
- name: Run Checkstyle
  run: ./gradlew checkstyleMain checkstyleTest
```

---

#### 🔹 3. **PMD & SpotBugs**

Run static code analysis to catch code smells, bad practices, and potential bugs.

> **Usage:**

```yaml
- name: Run PMD and SpotBugs
  run: ./gradlew pmdMain spotbugsMain
```

---

#### 🔹 4. **PIT Mutation Testing**

Ensures that your tests are meaningful and catch real bugs.

> **Usage:**

```yaml
- name: Run PIT Mutation Tests
  run: ./gradlew pitest
```

---

### 🛡️ **Security & Dependency Scanning**

---

#### 🔹 5. **OWASP Dependency-Check Action**

Scans dependencies for known CVEs.

> **Action:** `jeremylong/DependencyCheck`

```yaml
- name: OWASP Dependency Check
  uses: jeremylong/DependencyCheck@v6
  with:
    project: 'spring-boot-app'
    scan: './'
```

🔗 [https://github.com/jeremylong/DependencyCheck](https://github.com/jeremylong/DependencyCheck)

---

#### 🔹 6. **Snyk for Open Source Scanning**

Detects vulnerabilities and license issues in your dependencies.

> **Action:** `snyk/actions/gradle`

```yaml
- name: Snyk Scan
  uses: snyk/actions/gradle@master
  with:
    command: test
  env:
    SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}
```

🔗 [https://github.com/snyk/actions](https://github.com/snyk/actions)

---

### 🔍 **Code Smells & Technical Debt**

---

#### 🔹 7. **SonarCloud Scan**

Run full code analysis on PRs and push results to the Sonar dashboard.

> **Action:** `sonarsource/sonarcloud-github-action`

```yaml
- name: SonarCloud Analysis
  uses: sonarsource/sonarcloud-github-action@v2
  with:
    organization: my-org
    projectKey: my-org_springboot-project
  env:
    SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
```

🔗 [https://github.com/SonarSource/sonarcloud-github-action](https://github.com/SonarSource/sonarcloud-github-action)

---

### 📦 **Dependency Maintenance**

---

#### 🔹 8. **Gradle Dependency Updates**

Auto-checks for newer versions of dependencies.

> **Action:** `ben-manes/gradle-versions-plugin`

```yaml
- name: Check for Dependency Updates
  run: ./gradlew dependencyUpdates
```

---

### 📈 **Performance / Benchmarking (Advanced Use)**

---

#### 🔹 9. **JMH Benchmarks (if you write performance tests)**

Run JMH benchmarks and compare results in PRs (can be custom setup).

> 🛠️ You'll need to write benchmarks using [JMH](https://openjdk.org/projects/code-tools/jmh/).

---

### 🧪 **Unit & Integration Testing**

---

#### 🔹 10. **Test with JaCoCo + Gradle**

Run all unit tests and generate coverage report.

```yaml
- name: Run Tests with Coverage
  run: ./gradlew test jacocoTestReport
```

---

### 📝 **Example Full Workflow (`.github/workflows/quality.yml`)**

```yaml
name: Code Quality CI

on:
  pull_request:
  push:
    branches: [main, develop]

jobs:
  quality-checks:
    runs-on: ubuntu-latest

    steps:
    - name: Checkout Code
      uses: actions/checkout@v3

    - name: Set up JDK
      uses: actions/setup-java@v3
      with:
        distribution: 'temurin'
        java-version: '17'

    - name: Spotless Check
      run: ./gradlew spotlessCheck

    - name: Checkstyle
      run: ./gradlew checkstyleMain checkstyleTest

    - name: PMD and SpotBugs
      run: ./gradlew pmdMain spotbugsMain

    - name: Run Tests with Coverage
      run: ./gradlew test jacocoTestReport

    - name: Dependency Check (OWASP)
      uses: jeremylong/DependencyCheck@v6
      with:
        project: 'spring-boot-app'
        scan: './'

    - name: Snyk Scan
      uses: snyk/actions/gradle@master
      with:
        command: test
      env:
        SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}

    - name: Dependency Updates
      run: ./gradlew dependencyUpdates
```

---

## 🔐 **GitHub Secrets Configuration**

### **Required Secrets for GitHub Actions**

The workflows require the following secrets to be configured:

| Secret Name | Purpose | How to Get |
|-------------|---------|------------|
| `SNYK_TOKEN` | Snyk security scanning | [Snyk.io](https://snyk.io/) → Account Settings → API Tokens |
| `SONAR_TOKEN` | SonarCloud analysis | [SonarCloud](https://sonarcloud.io/) → Account → Security |
| `SONAR_HOST_URL` | SonarCloud host URL | Your SonarCloud instance URL (e.g., https://sonarcloud.io) |
| `DOCKER_USERNAME` | Docker Hub publishing | Your Docker Hub username |
| `DOCKER_PASSWORD` | Docker Hub publishing | [Docker Hub](https://hub.docker.com/) → Account Settings → Security (create access token) |

### **For Organization Secrets** (Vivid-Vortex-DevOps)

1. **Navigate to Organization Settings**
   - Go to: [Vivid-Vortex-DevOps Organization Secrets](https://github.com/organizations/Vivid-Vortex-DevOps/settings/secrets/actions)
   - Click on "Secrets and variables" → "Actions"

2. **Add Required Secrets**
   - Click "New repository secret" for each required secret
   - Configure repository access (All repositories or specific ones)

### **For Individual Repository Secrets**

1. **Navigate to Repository Settings**
   - Go to your repository → Settings → Secrets and variables → Actions
   - URL pattern: `https://github.com/Vivid-Vortex-DevOps/[repo-name]/settings/secrets/actions`

2. **Add the Same Secrets**
   - Click "New repository secret" for each required secret

### **Quick Setup Checklist**

- [ ] **Organization Level**: Add secrets at [Vivid-Vortex-DevOps Organization Secrets](https://github.com/organizations/Vivid-Vortex-DevOps/settings/secrets/actions)
- [ ] **Repository Level**: Add secrets in repository settings
- [ ] **Snyk Token**: Generate at [Snyk.io](https://snyk.io/)
- [ ] **SonarCloud Token**: Generate at [SonarCloud](https://sonarcloud.io/)
- [ ] **Docker Hub Token**: Create access token at [Docker Hub](https://hub.docker.com/)

### **Security Best Practices**

- Use **access tokens** instead of passwords for Docker Hub
- **Rotate tokens** regularly
- **Limit repository access** to only necessary repositories
- **Use organization secrets** for shared tokens across multiple repositories

---

Let me know if you want to **split this into multiple files**, add **report upload**, or generate **badges** for test coverage or quality gates!
