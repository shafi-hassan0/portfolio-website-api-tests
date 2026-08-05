package com.shafihassan.apitests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class ProjectsApiTest extends BaseApiTest {

  @Test
  void projectsEndpointReturnsAList() {
    given()
        .accept(ContentType.JSON)
    .when()
        .get("/api/projects")
    .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("success", equalTo(true))
        .body("data", notNullValue());
  }
}
