package com.shafihassan.apitests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class HealthApiTest extends BaseApiTest {

  @Test
  void healthEndpointReportsHealthy() {
    given()
        .accept(ContentType.JSON)
    .when()
        .get("/api/health")
    .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("success", equalTo(true))
        .body("data.status", equalTo("healthy"))
        .body("data.database", equalTo("connected"));
  }
}
