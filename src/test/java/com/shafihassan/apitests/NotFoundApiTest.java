package com.shafihassan.apitests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

class NotFoundApiTest extends BaseApiTest {

  @Test
  void unknownRouteReturns404() {
    given()
    .when()
        .get("/api/this-route-does-not-exist")
    .then()
        .statusCode(404);
  }
}
