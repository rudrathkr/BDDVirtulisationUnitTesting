# BDD Virtualization Testing

Enterprise-grade starter project for running BDD tests locally before integration with real infrastructure. This project demonstrates how to virtualize external dependencies (databases, APIs, message queues) for fast, reliable, and isolated BDD testing without requiring actual infrastructure services.

---

## Table of Contents

1. [What is Virtualization Testing?](#what-is-virtualization-testing)
2. [What Gets Virtualized?](#what-gets-virtualized)
3. [Prerequisites & Software Installation](#prerequisites--software-installation)
4. [Step-by-Step Local Setup Guide](#step-by-step-local-setup-guide)
5. [Running Tests](#running-tests)
6. [Test Reports & Logs](#test-reports--logs)
7. [Project Structure](#project-structure)
8. [Configuration & Customization](#configuration--customization)
9. [Troubleshooting](#troubleshooting)
10. [Advanced Topics](#advanced-topics)

---

## What is Virtualization Testing?

Virtualization testing is a technique where external dependencies (databases, APIs, message queues) are replaced with lightweight, in-memory alternatives running locally on your machine. This allows you to:

- **Run tests offline** - No internet or external services needed
- **Run tests faster** - No network latency or service startup delays
- **Run tests reliably** - Complete isolation and reproducibility
- **Run tests early** - Execute during local development before CI/CD
- **Run tests independently** - No conflicts with other teams' environments

---

## What Gets Virtualized?

| Integration Type | Real Service | Local Virtualization |
|---|---|---|
| RDBMS (Oracle/MySQL/PostgreSQL/SQL Server) | Database Server | H2 In-Memory Database |
| REST APIs | External API Server | WireMock Mock Server |
| Message Queues | ActiveMQ/RabbitMQ Broker | Embedded ActiveMQ Broker |
| JSON Response Validation | Manual or basic tools | JSONAssert Library |
| XML Response Validation | Manual or basic tools | XMLUnit Library |

---

## Prerequisites & Software Installation

### Step 1: Verify/Install Java 17+

Check if Java is installed and meets the version requirement:

```bash
java -version
```

**Expected Output:** Java 17 or higher

**If Java is NOT installed:**

**macOS (using Homebrew):**
```bash
brew install openjdk@17
# Set JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
# Add to ~/.zshrc or ~/.bash_profile for persistence
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
source ~/.zshrc
```

**Windows (using Chocolatey):**
```bash
choco install openjdk17
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt-get update
sudo apt-get install openjdk-17-jdk
```

### Step 2: Verify/Install Maven 3.9+

Check if Maven is installed and meets the version requirement:

```bash
mvn --version
```

**Expected Output:** Maven 3.9.0 or higher with Java 17

**If Maven is NOT installed:**

**macOS (using Homebrew):**
```bash
brew install maven
mvn --version
```

**Windows (using Chocolatey):**
```bash
choco install maven
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt-get install maven
```

**Manual Installation (Any OS):**
- Download from: https://maven.apache.org/download.cgi
- Extract to your preferred directory
- Add `bin` folder to PATH environment variable

### Step 3: Verify Git Installation (for cloning the repository)

```bash
git --version
```

**If NOT installed, download from:** https://git-scm.com/downloads

---

## Step-by-Step Local Setup Guide

### Step 1: Clone the Repository

Navigate to your preferred workspace directory and clone the project:

```bash
cd ~/your-workspace
git clone https://github.com/your-org/bdd-virtualization-testing.git
cd bdd-virtualization-testing
```

### Step 2: Verify Project Structure

Confirm the project structure is intact:

```bash
ls -la
```

**Expected directories:**
- `src/` - Source code and test files
- `target/` - Build output (generated after first build)
- `pom.xml` - Maven configuration file
- `README.md` - This documentation

### Step 3: Clean and Build the Project

This step compiles the code, downloads dependencies, and validates everything is setup correctly:

```bash
mvn clean install
```

**What happens during this command:**
- `clean` - Removes any previous build artifacts in the `target/` directory
- `install` - Downloads dependencies from Maven Central Repository, compiles source code, runs all tests, and installs the JAR in your local Maven repository

**Expected output (partial):**
```
[INFO] Downloading from central: ... (dependencies will be downloaded)
[INFO] Compiling 4 source files ...
[INFO] Compiling 9 source files ...
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**First-time build may take 2-3 minutes** as Maven downloads all dependencies (~200MB).

### Step 4: Verify Installation Success

If the build completes with `BUILD SUCCESS`, your local environment is properly configured. You should see:
- ✅ All 7 tests passed
- ✅ JAR file created in `target/` directory
- ✅ No compilation errors

---

## Running Tests

### 1. Run All Tests (Full Test Suite)

Execute all 7 BDD test scenarios including database, REST API, and messaging tests:

```bash
mvn clean test
```

**Duration:** ~3-5 seconds

**What it tests:**
- Database operations (insert, read, delete)
- REST API calls with JSON/XML validation
- ActiveMQ message publishing and consumption

### 2. Run Tests by Cucumber Tag (Selective Execution)

Execute only specific test scenarios based on tags:

**Run only Regression tests:**
```bash
mvn test -Dcucumber.filter.tags="@Regression"
```

**Run only JulyRelease tests:**
```bash
mvn test -Dcucumber.filter.tags="@JulyRelease"
```

**Run only Messaging tests:**
```bash
mvn test -Dcucumber.filter.tags="@Messaging"
```

**Run multiple tags (OR operation):**
```bash
mvn test -Dcucumber.filter.tags="@Regression or @Messaging"
```

**Run multiple tags (AND operation):**
```bash
mvn test -Dcucumber.filter.tags="@Regression and @JulyRelease"
```

### 3. Run Tests with Verbose Output

For detailed debugging information:

```bash
mvn test -X
```

This provides complete diagnostic information including dependency resolution and compilation details.

### 4. Build Without Running Tests

If you only want to compile and install without executing tests:

```bash
mvn clean install -DskipTests
```

---

## Test Reports & Logs

### 1. HTML Test Report

After test execution, view the HTML report:

```bash
open target/cucumber-report.html
# On Windows: start target/cucumber-report.html
# On Linux: xdg-open target/cucumber-report.html
```

This report shows:
- ✅ Passed/Failed/Skipped tests
- 📊 Test execution timeline
- 📝 Detailed scenario descriptions
- 🔗 Step-by-step execution flow

### 2. JSON Test Report

Programmatic access to test results (useful for CI/CD integration):

```bash
cat target/cucumber-report.json
```

### 3. Maven Surefire Reports

Detailed test execution reports:

```bash
target/surefire-reports/
```

Browse the generated XML and text files for detailed test metrics.

### 4. Console Output Logs

View real-time test execution logs:

```bash
mvn test -Dorg.slf4j.simpleLogger.defaultLogLevel=INFO
```

---

## Project Structure

```
bdd-virtualization-testing/
├── src/
│   ├── main/java/com/company/person/
│   │   ├── client/
│   │   │   └── PersonRestClient.java          # REST client to call mocked APIs
│   │   ├── model/
│   │   │   └── Person.java                    # Person POJO model
│   │   ├── repository/
│   │   │   └── PersonRepository.java          # Database repository layer
│   │   └── service/
│   │       └── PersonService.java             # Business logic service
│   │
│   └── test/
│       ├── java/com/company/person/bdd/
│       │   ├── config/
│       │   │   └── VirtualizedTestContext.java    # Virtualizes H2, WireMock, ActiveMQ
│       │   ├── db/
│       │   │   └── TestDatabaseInitializer.java   # Creates schema and seeds test data
│       │   ├── hooks/
│       │   │   └── BddHooks.java                  # Setup/teardown before/after tests
│       │   ├── mq/
│       │   │   ├── EmbeddedActiveMqConfig.java    # Embedded ActiveMQ configuration
│       │   │   ├── PersonMessageConsumer.java     # Consumes messages from queue
│       │   │   └── PersonMessageProducer.java     # Publishes messages to queue
│       │   ├── rest/
│       │   │   └── WireMockStubs.java             # Mock REST API responses
│       │   ├── runner/
│       │   │   └── CucumberTestRunner.java        # Cucumber test runner
│       │   └── steps/
│       │       └── PersonSteps.java               # Gherkin step implementations
│       │
│       └── resources/
│           ├── db/
│           │   ├── schema.sql                     # H2 database schema
│           │   └── test-data.sql                  # Test data for Person records
│           ├── expected/
│           │   ├── Response1.json                 # Expected Rudra Thakur JSON
│           │   ├── Response2.json                 # Expected Raj Kumar JSON
│           │   ├── Response1.xml                  # Expected Rudra Thakur XML
│           │   └── Response2.xml                  # Expected Raj Kumar XML
│           └── features/
│               └── person.feature                 # Gherkin BDD scenarios
│
├── target/                                        # Build output (generated)
│   ├── classes/                                   # Compiled source files
│   ├── test-classes/                              # Compiled test files
│   ├── cucumber-report.html                       # Test execution report
│   ├── cucumber-report.json                       # Test results in JSON
│   └── surefire-reports/                          # Maven test reports
│
├── pom.xml                                        # Maven dependencies and plugins
└── README.md                                      # This documentation
```

### Key Files Explained

| File | Purpose |
|---|---|
| `person.feature` | Contains BDD test scenarios in Gherkin language |
| `PersonSteps.java` | Implements the "Given", "When", "Then" steps from the feature file |
| `VirtualizedTestContext.java` | Initializes H2 database, WireMock server, and ActiveMQ broker |
| `WireMockStubs.java` | Defines mock REST API responses for different test cases |
| `PersonRepository.java` | JDBC layer for database operations |
| `PersonRestClient.java` | HTTP client that calls the mocked REST endpoints |
| `EmbeddedActiveMqConfig.java` | Starts an embedded ActiveMQ broker for messaging tests |

---

## Configuration & Customization

### 1. Change Database Mode

The H2 database supports multiple SQL dialects. To change from the default mode, edit `VirtualizedTestContext.java`:

**Current (PostgreSQL mode):**
```java
String url = "jdbc:h2:mem:persondb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1";
```

**Other supported modes:**

**Oracle Mode:**
```java
String url = "jdbc:h2:mem:persondb;MODE=Oracle;DB_CLOSE_DELAY=-1";
```

**MySQL Mode:**
```java
String url = "jdbc:h2:mem:persondb;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1";
```

**SQL Server Mode:**
```java
String url = "jdbc:h2:mem:persondb;MODE=MSSQLServer;DB_CLOSE_DELAY=-1";
```

### 2. Modify Test Data

Edit `src/test/resources/db/test-data.sql` to change the seed data used in tests:

```sql
INSERT INTO PERSON
(FIRST_NAME, LAST_NAME, PROFESSION, LOCATION_X, LOCATION_Y, COMPANY_ORG, COMPANY_HQ)
VALUES
('YourName', 'YourSurname', 'YourRole', 100, 150, 'YourCompany', 'YourLocation');
```

### 3. Add New Mock API Endpoints

Edit `WireMockStubs.java` to add new REST API mocks:

```java
stubFor(get(urlPathEqualTo("/persons/json/FirstName/LastName"))
        .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                      "firstName": "FirstName",
                      "lastName": "LastName",
                      "profession": "Role",
                      "companyOrg": "Company",
                      "companyHeadQuarters": "Location"
                    }
                    """)));
```

### 4. Adjust WireMock Port

To use a different port for the mocked REST server, edit `VirtualizedTestContext.java`:

```java
private static final int WIREMOCK_PORT = 8888; // Default is 8081
```

### 5. Modify ActiveMQ Configuration

To change the embedded ActiveMQ broker configuration, edit `EmbeddedActiveMqConfig.java`:

```java
String brokerUrl = "tcp://localhost:61616"; // Default broker URL
```

---

## Troubleshooting

### Issue 1: Java Version Mismatch

**Error:**
```
[ERROR] FAILURE! Compilation failed.
error: java version "11.0.20" is not supported
```

**Solution:**
```bash
# Check current Java version
java -version

# Set Java 17 as default (macOS)
export JAVA_HOME=$(/usr/libexec/java_home -v 17)

# Verify
java -version
```

### Issue 2: Maven Not Found

**Error:**
```
command not found: mvn
```

**Solution:**
```bash
# Check Maven installation
mvn --version

# If not found, install Maven (macOS)
brew install maven

# Add to PATH if manually installed
export PATH=$PATH:/path/to/maven/bin
```

### Issue 3: Build Fails with Dependency Download Errors

**Error:**
```
[ERROR] Failed to execute goal on project bdd-virtualization-testing
[ERROR] Could not find artifact ...
```

**Solution:**
```bash
# Clear Maven cache and retry
rm -rf ~/.m2/repository
mvn clean install

# Or force update
mvn clean install -U
```

### Issue 4: Port Already in Use

**Error:**
```
Address already in use: bind
```

**Solution:**
```bash
# Find process using port 8081 (WireMock)
lsof -i :8081

# Kill the process (get PID from above)
kill -9 <PID>

# Then retry tests
mvn clean test
```

### Issue 5: Tests Fail with "Cannot Find Symbol"

**Error:**
```
[ERROR] cannot find symbol: class ConnectionFactory
```

**Solution:**
```bash
# Ensure all dependencies are downloaded
mvn dependency:resolve

# Clean and rebuild
mvn clean install -U

# Check pom.xml for missing dependencies
```

### Issue 6: H2 Database SQL Syntax Error

**Error:**
```
Syntax error in SQL statement "CREATE TABLE PERSON..."
```

**Solution:**
Ensure schema.sql uses H2 compatible syntax:
```sql
-- Use GENERATED ALWAYS AS IDENTITY instead of AUTO_INCREMENT
CREATE TABLE PERSON (
    ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    FIRST_NAME VARCHAR(50),
    ...
);
```

---

## Advanced Topics

### 1. Running Tests in CI/CD Pipeline

For Jenkins, GitHub Actions, GitLab CI, or other CI/CD systems:

```bash
# Ensure Maven is available in CI environment
mvn clean install

# Generate reports for CI dashboard integration
mvn test -Dgenerate.reports=true
```

### 2. Integrating with IDE

**IntelliJ IDEA / JetBrains:**
1. Open the project folder
2. Right-click on `person.feature` file
3. Click "Run" to execute BDD tests
4. View results in the "Run" panel

**Visual Studio Code:**
1. Install "Cucumber for Java" extension
2. Open `person.feature` file
3. Click on the play icon to run scenarios

**Eclipse:**
1. Install Cucumber Eclipse Plugin
2. Right-click on feature file
3. Select "Run As" > "Cucumber Test"

### 3. Implementing Additional Test Scenarios

To add new BDD scenarios:

1. **Add scenario to `person.feature`:**
```gherkin
@MyTag
Scenario: My New Test Case
  Given some precondition
  When some action happens
  Then verify the result
```

2. **Implement steps in `PersonSteps.java`:**
```java
@Given("some precondition")
public void somePrecondition() {
    // Implementation
}
```

3. **Run the new test:**
```bash
mvn test -Dcucumber.filter.tags="@MyTag"
```

### 4. Extending Virtualizations

To virtualize additional services:

1. **Add new dependency in `pom.xml`**
2. **Create configuration class similar to `VirtualizedTestContext.java`**
3. **Initialize in `BddHooks.beforeAll()` method**
4. **Create step implementations in `PersonSteps.java`**

### 5. Performance Optimization

For faster test execution:

```bash
# Run tests in parallel (if supported)
mvn test -T 1C  # Uses 1 thread per core

# Skip code generation
mvn test -DskipCodeGen=true

# Cache dependencies
mvn dependency:go-offline
```

---

## Support & Resources

- **Maven Documentation:** https://maven.apache.org/guides/
- **Cucumber Documentation:** https://cucumber.io/docs/cucumber/
- **H2 Database Documentation:** https://h2database.com/
- **WireMock Documentation:** https://wiremock.org/docs/
- **ActiveMQ Documentation:** https://activemq.apache.org/documentation

---

## Summary

This BDD Virtualization Testing project provides a complete framework for local testing without external dependencies. Follow this guide sequentially to set up your environment and start running tests immediately.

**Quick Start (TL;DR):**
```bash
# Step 1: Install Java 17 and Maven 3.9+
# Step 2: Clone the repository
git clone https://github.com/your-org/bdd-virtualization-testing.git
cd bdd-virtualization-testing

# Step 3: Build and run tests
mvn clean install

# Step 4: View results
open target/cucumber-report.html
```

## Report

<img width="1538" height="857" alt="Screenshot 2026-05-06 at 5 32 16 PM" src="https://github.com/user-attachments/assets/0d1cbe4a-6a85-4800-ae44-1841a91415bf" />
