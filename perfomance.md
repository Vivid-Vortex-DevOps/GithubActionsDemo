# Performance & Security Analysis

## 🔒 **Security Scanning Tools**

### **Trivy FS Scan**
Trivy is a comprehensive security scanner that performs file system analysis to detect:
- **Vulnerabilities** in OS packages and software dependencies
- **Misconfigurations** in Infrastructure as Code (IaC) files
- **Secret leaks** in code and configuration files
- **Security issues** in Dockerfiles, Kubernetes manifests, and other config files

**Installation:**
```bash
curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -s -- -b /usr/local/bin v0.48.0
```

**Usage in GitHub Actions:**
```yaml
- name: Install Trivy
  run: |
    curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -s -- -b /usr/local/bin v0.48.0
    trivy --version
    
- name: Run Trivy FS Scan
  run: |
    trivy fs --format json --output trivy-fs-report.json .
    trivy fs --severity HIGH,CRITICAL --exit-code 1 .
```

### **Gitleaks Secret Detection**
Gitleaks is a specialized tool for detecting hardcoded secrets and sensitive information in git repositories.

**Detects:**
- API keys and access tokens
- Database credentials
- Private keys and certificates
- AWS/GCP/Azure credentials
- GitHub tokens and other service tokens

### **CodeQL Semantic Analysis**
CodeQL is GitHub's semantic code analysis engine that finds security vulnerabilities and coding errors by understanding code context.

**Capabilities:**
- **Semantic analysis** - understands code context better than pattern matching
- **Security vulnerabilities** - SQL injection, XSS, path traversal, etc.
- **Coding errors** - null pointer dereferences, resource leaks, etc.
- **Custom queries** - can write custom security rules
- **Native GitHub integration** - results appear in Security tab

**Installation:**
```bash
curl -sSfL https://raw.githubusercontent.com/zricethezav/gitleaks/master/install.sh | sh -s -- -b /usr/local/bin v8.18.0
```

**Usage in GitHub Actions:**
```yaml
- name: Install Gitleaks
  run: |
    curl -sSfL https://raw.githubusercontent.com/zricethezav/gitleaks/master/install.sh | sh -s -- -b /usr/local/bin v8.18.0
    gitleaks version
    
- name: Run Gitleaks Scan
  run: |
    gitleaks detect --source . --report-format json --report-path gitleaks-report.json --exit-code 0
```

**CodeQL Usage in GitHub Actions:**
```yaml
- name: Initialize CodeQL
  uses: github/codeql-action/init@v3
  with:
    languages: java
    queries: security-extended,security-and-quality
    
- name: Autobuild
  uses: github/codeql-action/autobuild@v3
  
- name: Perform CodeQL Analysis
  uses: github/codeql-action/analyze@v3
  with:
    category: "/language:java"
```

## 📊 **Performance Testing**

### **JMH Benchmarks**
The project includes JMH (Java Microbenchmark Harness) benchmarks for performance testing:

**Location:** `src/test/java/com/demo/actions/GithubActionsDemo/performance/UserServiceBenchmark.java`

**Running Benchmarks:**
```bash
./gradlew jmh
```

**Benchmark Results:**
- Results are generated in `build/reports/jmh/`
- JSON format for easy parsing and analysis
- HTML reports for human-readable output

### **Performance Metrics Tracked**
1. **Response Time**: Average, median, and percentile response times
2. **Throughput**: Operations per second
3. **Memory Usage**: Heap and non-heap memory consumption
4. **CPU Usage**: CPU cycles and utilization

## 🔍 **Quality Gates**

### **Security Quality Gates**
- **Trivy FS**: No HIGH or CRITICAL vulnerabilities
- **Gitleaks**: No hardcoded secrets detected
- **CodeQL**: No security vulnerabilities or coding errors
- **Snyk**: No high-severity dependency vulnerabilities
- **TruffleHog**: No secrets in recent commits

### **Performance Quality Gates**
- **Response Time**: < 100ms for 95th percentile
- **Throughput**: > 1000 ops/sec for critical operations
- **Memory**: < 512MB heap usage under load
- **Test Coverage**: > 80% line coverage

## 📈 **Monitoring & Reporting**

### **Artifacts Generated**
1. **trivy-fs-report.json**: Detailed vulnerability report
2. **gitleaks-report.json**: Secret detection findings
3. **CodeQL results**: Available in GitHub Security tab
4. **benchmark-results/**: JMH performance metrics
5. **quality-reports/**: All quality check results

### **Report Retention**
- All reports are retained for 30 days
- Accessible via GitHub Actions artifacts
- Can be downloaded for offline analysis

## 🛠️ **Configuration**

### **Trivy Configuration**
```yaml
# .trivyignore (optional)
# Add patterns to ignore specific files/directories
node_modules/
*.log
```

### **Gitleaks Configuration**
```toml
# .gitleaks.toml (optional)
[allowlist]
description = "Allowlist for false positives"
[[allowlist.regexes]]
regex = '''AKIA[0-9A-Z]{16}'''
reason = "AWS Access Key ID (false positive)"
```

## 🚀 **Best Practices**

### **Security Scanning**
1. Run scans on every PR and push
2. Use `--exit-code 1` for critical issues
3. Generate JSON reports for automation
4. Review and address findings promptly

### **Performance Testing**
1. Run benchmarks in CI/CD pipeline
2. Compare results across commits
3. Set performance regression alerts
4. Monitor trends over time

### **Integration**
1. Integrate with existing quality gates
2. Use reports for compliance documentation
3. Automate issue creation for critical findings
4. Include security scanning in deployment pipelines
