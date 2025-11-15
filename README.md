# Selenium Docker Compose Demo

This repository demonstrates how to set up and run Selenium tests using Docker Compose for both standalone and Selenium Grid configurations. This is ideal for running automated browser tests in isolated containers, ensuring consistency across environments.

## Project Structure

```
.
├── .github/              # GitHub workflows (CI/CD)
├── .idea/                # IDE project settings
├── docker/               # Docker Compose configurations
│   ├── docker-compose_grid.yml
│   └── docker-compose_standalone.yml
├── logs/                 # Execution logs
├── reports/              # Test reports
├── screenshot/           # Test screenshots
├── src/
│   ├── main/             # (Application code, if any)
│   └── test/             # Test source code
├── pom.xml               # Maven dependencies and build config
├── testng.xml            # TestNG test suite configuration
├── testng_maven.xml      # TestNG-Maven configuration
└── .gitignore
```

---

## Prerequisites

- [Docker](https://www.docker.com/products/docker-desktop)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java 8+](https://adoptopenjdk.net/)
- [Maven](https://maven.apache.org/)

---


## How It Works

The project provides two Docker Compose files under the `docker/` directory:

- **docker-compose_grid.yml**: Sets up Selenium Grid with hub and browser nodes (Chrome, Firefox). Use this for distributed parallel execution.
- **docker-compose_standalone.yml**: Sets up a single standalone Selenium browser container (e.g., Chrome). Use this for quick local runs.

The Java tests (in `src/test`) are configured via TestNG and can be run locally or against the Selenium containers provided.

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/Madhiseelan/selenium-dockercompose-demo.git
cd selenium-dockercompose-demo
```

### 2. Start Selenium via Docker Compose

#### a. For Selenium Standalone:

```bash
docker-compose -f docker/docker-compose_standalone.yml up -d
```

#### b. For Selenium Grid:

```bash
docker-compose -f docker/docker-compose_grid.yml up -d
```

This will spin up the necessary containers in the background.

### 3. Configure Test Execution

- Update the Selenium Hub/Standalone URL in your test code or properties as needed (usually `http://localhost:4444/wd/hub` for grid or standalone).

### 4. Run the Tests

#### Using Maven

```bash
mvn clean test
```

- The tests are defined using TestNG and pickup configurations from `testng.xml` or `testng_maven.xml`.
- Reports will be generated under the `reports/` directory.
- Logs can be found in the `logs/` folder.
- Any screenshots captured on failure can be found in the `screenshot/` folder.

### 5. Tear Down

After test execution, shutdown the containers:

```bash
docker-compose -f docker/docker-compose_standalone.yml down
# OR
docker-compose -f docker/docker-compose_grid.yml down
```

---

## CI/CD Workflow

- The repository contains a `.github/workflows` directory, indicating GitHub Actions workflows for CI/CD.
- These workflows automate build/test processes, ensuring code is validated on each commit.

---


## Customizing Tests

1. Add your test classes to `src/test`.
2. Modify `testng.xml` to include/exclude specific tests or packages.
3. Adjust browser/container configuration in the respective docker-compose YAML as needed.

---

## Troubleshooting

- Ensure Docker containers are running (`docker ps`).
- Check logs in `logs/` and container logs (`docker logs <container>`) for troubleshooting.
- Verify correct browser/container version compatibility if using custom images.

---
