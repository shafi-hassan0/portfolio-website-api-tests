# Portfolio API Test Suite

A REST Assured / JUnit test suite covering the [Portfolio REST API](https://github.com/shafi-hassan0/portfolio-website-api) — the backend behind [shafihassan.com](https://shafihassan.com).

## Highlights

- Positive **and** negative coverage across every endpoint: validation errors, not-found on both valid and malformed IDs, and unknown routes
- A tag-gated live test for the contact form, kept out of the default run so a real email only sends when triggered deliberately
- Runs automatically on every push to the API, nightly against production, and on every pull request — with results reported back to the API repo as a commit status
- Part of a fully automated cross-repo pipeline: an API deploy dispatches this suite and waits for the real pass/fail before the deploy is considered successful

---

## For Developers

### Stack

- Java 21
- Maven
- REST Assured 6
- JUnit Jupiter 6

### Running locally

```bash
mvn test
```

By default, tests run against production (`https://shafihassan.com`). To point at a different environment:

```bash
mvn test -Dapi.baseUri=http://localhost:3000
```

### CI

GitHub Actions runs the suite on every push/PR to `main` and nightly against production, so a broken deploy gets caught without needing a manual check. See `.github/workflows/tests.yml`.

### The contact form's positive test

`ContactApiTest.acceptsAValidSubmission` is tagged `live-write` and excluded from the default run — a real success there creates a real `Contact` document and sends a real email through EmailJS, which shouldn't happen on every push or nightly run.

Run it deliberately, either from the **Contact Form Live Test** workflow in the Actions tab (`workflow_dispatch`, manual trigger only), or locally:

```bash
mvn test -Plive-write -Dtest=ContactApiTest
```
