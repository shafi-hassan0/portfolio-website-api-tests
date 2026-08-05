package com.shafihassan.apitests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;

class SkillsApiTest extends BaseApiTest {

  @Test
  void listReturnsPublishedSkills() {
    given()
        .accept(ContentType.JSON)
    .when()
        .get("/api/skills")
    .then()
        .statusCode(200)
        .body("success", equalTo(true))
        .body("data", not(empty()));
  }

  @Test
  void listCanBeFilteredByType() {
    given()
        .accept(ContentType.JSON)
        .queryParam("type", "framework")
    .when()
        .get("/api/skills")
    .then()
        .statusCode(200)
        .body("success", equalTo(true))
        .body("data.skillType", everyItem(equalTo("framework")));
  }

  @Test
  void getByIdReturnsTheSkill() {
    String id =
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/api/skills")
        .then()
            .statusCode(200)
        .extract()
            .path("data[0]._id");

    given()
        .accept(ContentType.JSON)
    .when()
        .get("/api/skills/{id}", id)
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
        .get("/api/skills/does-not-exist")
    .then()
        .statusCode(404)
        .body("success", equalTo(false))
        .body("error.code", equalTo("NOT_FOUND"));
  }
}
