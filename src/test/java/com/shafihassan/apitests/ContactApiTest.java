package com.shafihassan.apitests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class ContactApiTest extends BaseApiTest {

  @Test
  void rejectsAMissingField() {
    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("""
            {"name": "Test User", "message": "Hello"}
            """)
    .when()
        .post("/api/contact")
    .then()
        .statusCode(400)
        .body("success", equalTo(false))
        .body("error.code", equalTo("VALIDATION_ERROR"));
  }

  @Test
  void rejectsAnInvalidEmailFormat() {
    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("""
            {"name": "Test User", "email": "not-an-email", "message": "Hello"}
            """)
    .when()
        .post("/api/contact")
    .then()
        .statusCode(400)
        .body("success", equalTo(false))
        .body("error.code", equalTo("VALIDATION_ERROR"));
  }

  /**
   * Tagged "live-write" and excluded from the default run: a real success here
   * creates a real Contact document and sends a real email through EmailJS.
   * Run deliberately via the "Contact Form Live Test" GitHub Actions workflow,
   * or locally with {@code mvn test -Dsurefire.excludedGroups= -Dgroups=live-write}.
   */
  @Test
  @Tag("live-write")
  void acceptsAValidSubmission() {
    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("""
            {"name": "REST Assured Live Test", "email": "test@example.com", "message": "Automated positive-path test of POST /api/contact."}
            """)
    .when()
        .post("/api/contact")
    .then()
        .statusCode(200)
        .body("success", equalTo(true))
        .body("data.message", equalTo("Message sent successfully"));
  }
}
