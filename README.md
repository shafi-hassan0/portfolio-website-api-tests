# portfolio-api-tests

REST Assured API test suite for the backend behind [shafihassan.com](https://shafihassan.com) — a Node/Express/MongoDB API deployed via Docker behind a Cloudflare Tunnel.

## Stack

- Java 21
- Maven
- REST Assured 6
- JUnit Jupiter 6

## Running locally

```bash
mvn test
```

By default, tests run against production (`https://shafihassan.com`). To point at a different environment:

```bash
mvn test -Dapi.baseUri=http://localhost:3000
```

## CI

GitHub Actions runs the suite on every push/PR to `main` and nightly against production, so a broken deploy gets caught without needing a manual check. See `.github/workflows/tests.yml`.
