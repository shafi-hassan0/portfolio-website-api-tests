package com.shafihassan.apitests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class NowApiTest extends BaseApiTest {

  @Test
  void nowReturnsTheCurrentUpdate() {
    given()
        .accept(ContentType.JSON)
    .when()
        .get("/api/now")
    .then()
        .statusCode(200)
        .body("success", equalTo(true))
        .body("data._id", notNullValue());
  }
}
