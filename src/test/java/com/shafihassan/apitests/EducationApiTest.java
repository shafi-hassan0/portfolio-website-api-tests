package com.shafihassan.apitests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;

class EducationApiTest extends BaseApiTest {

  @Test
  void listReturnsPublishedEducation() {
    given()
        .accept(ContentType.JSON)
    .when()
        .get("/api/education")
    .then()
        .statusCode(200)
        .body("success", equalTo(true))
        .body("data", not(empty()));
  }
}
