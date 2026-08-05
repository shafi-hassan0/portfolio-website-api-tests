package com.shafihassan.apitests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;

class ExperiencesApiTest extends BaseApiTest {

  @Test
  void listReturnsPublishedExperiences() {
    given()
        .accept(ContentType.JSON)
    .when()
        .get("/api/experiences")
    .then()
        .statusCode(200)
        .body("success", equalTo(true))
        .body("data", not(empty()));
  }

  @Test
  void getByIdReturnsTheExperience() {
    String id =
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/api/experiences")
        .then()
            .statusCode(200)
        .extract()
            .path("data[0]._id");

    given()
        .accept(ContentType.JSON)
    .when()
        .get("/api/experiences/{id}", id)
    .then()
        .statusCode(200)
        .body("success", equalTo(true))
        .body("data._id", equalTo(id));
  }

  @Test
  void getByIdReturns404ForAnUnknownId() {
    given()
        .accept(ContentType.JSON)
    .when()
        .get("/api/experiences/does-not-exist")
    .then()
        .statusCode(404)
        .body("success", equalTo(false))
        .body("error.code", equalTo("NOT_FOUND"));
  }
}
