package com.shafihassan.apitests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;

class StoryChaptersApiTest extends BaseApiTest {

  @Test
  void listReturnsPublishedChapters() {
    given()
        .accept(ContentType.JSON)
    .when()
        .get("/api/story-chapters")
    .then()
        .statusCode(200)
        .body("success", equalTo(true))
        .body("data", not(empty()));
  }

  @Test
  void getByIdReturnsTheChapter() {
    String id =
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/api/story-chapters")
        .then()
            .statusCode(200)
        .extract()
            .path("data[0]._id");

    given()
        .accept(ContentType.JSON)
    .when()
        .get("/api/story-chapters/{id}", id)
    .then()
        .statusCode(200)
        .body("success", equalTo(true))
        .body("data._id", equalTo(id));
  }

  @Test
  void getByIdReturns404ForAWellFormedButUnknownId() {
    // A syntactically valid Mongo ObjectId that doesn't belong to any document.
    given()
        .accept(ContentType.JSON)
    .when()
        .get("/api/story-chapters/000000000000000000000000")
    .then()
        .statusCode(404)
        .body("success", equalTo(false))
        .body("error.code", equalTo("NOT_FOUND"))
        .body("error.message", equalTo("Story chapter not found"));
  }

  @Test
  void getByIdReturns404ForAMalformedId() {
    // Not a valid ObjectId shape at all, so Mongoose throws a CastError,
    // which the app's generic error handler turns into a 404 with a
    // different message than the route's own "not found" response above.
    given()
        .accept(ContentType.JSON)
    .when()
        .get("/api/story-chapters/not-a-valid-id")
    .then()
        .statusCode(404)
        .body("success", equalTo(false))
        .body("error.code", equalTo("NOT_FOUND"))
        .body("error.message", equalTo("Resource not found"));
  }
}
