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

### The contact form's positive test

`ContactApiTest.acceptsAValidSubmission` is tagged `live-write` and excluded from the default run — a real success there creates a real `Contact` document and sends a real email through EmailJS, which shouldn't happen on every push or nightly run.

Run it deliberately, either from the **Contact Form Live Test** workflow in the Actions tab (`workflow_dispatch`, manual trigger only), or locally:

```bash
mvn test -Plive-write -Dtest=ContactApiTest
```
