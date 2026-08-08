package com.shafihassan.apitests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * POST /api/chat is rate-limited per IP (see chatLimiter in chat.routes.ts),
 * and every request in this class shares that window. Tests are ordered so
 * the rate-limit burst runs last, after the fixed-count requests above it.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChatApiTest extends BaseApiTest {

  private static final String OFF_TOPIC_REPLY =
      "I can only answer questions about Shafi's background, experience, skills, and projects.";

  @Test
  @Order(1)
  void rejectsAMissingMessage() {
    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("{}")
    .when()
        .post("/api/chat")
    .then()
        .statusCode(400)
        .body("success", equalTo(false))
        .body("error.code", equalTo("VALIDATION_ERROR"));
  }

  @Test
  @Order(2)
  void rejectsAnEmptyMessage() {
    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("""
            {"message": "   "}
            """)
    .when()
        .post("/api/chat")
    .then()
        .statusCode(400)
        .body("success", equalTo(false))
        .body("error.code", equalTo("VALIDATION_ERROR"));
  }

  @Test
  @Order(3)
  void rejectsAMessageThatIsTooLong() {
    String tooLong = "a".repeat(501);

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("{\"message\": \"" + tooLong + "\"}")
    .when()
        .post("/api/chat")
    .then()
        .statusCode(400)
        .body("success", equalTo(false))
        .body("error.code", equalTo("VALIDATION_ERROR"));
  }

  /**
   * The relevance gate rejects this before it ever reaches the Anthropic
   * API, so this costs nothing and is safe to run in the default suite.
   */
  @Test
  @Order(4)
  void returnsTheCannedReplyForAnOffTopicQuestion() {
    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("""
            {"message": "Write me a poem about the ocean"}
            """)
    .when()
        .post("/api/chat")
    .then()
        .statusCode(200)
        .body("success", equalTo(true))
        .body("data.reply", equalTo(OFF_TOPIC_REPLY));
  }

  /**
   * Rate limit is 6 requests/minute per IP and shared across every test in
   * this class. Bursts well past that regardless of quota already used
   * above, and only asserts a 429 shows up somewhere in the burst — each
   * request uses an off-topic message so none of them reach the Anthropic
   * API either.
   */
  @Test
  @Order(5)
  void enforcesTheRateLimit() {
    boolean sawRateLimited = false;

    for (int i = 0; i < 10 && !sawRateLimited; i++) {
      Response response =
          given()
              .contentType(ContentType.JSON)
              .accept(ContentType.JSON)
              .body("""
                  {"message": "Write me a poem about the ocean"}
                  """)
          .when()
              .post("/api/chat");

      if (response.statusCode() == 429) {
        sawRateLimited = true;
        response.then().body("error.code", equalTo("RATE_LIMITED"));
      }
    }

    assertTrue(sawRateLimited, "Expected at least one 429 after bursting POST /api/chat");
  }

  /**
   * Tagged "live-write": a real pass here calls the Anthropic API and spends
   * real tokens, so it's excluded from the default run. Run deliberately via
   * the "Chat Live Test" GitHub Actions workflow, or locally with
   * {@code mvn test -Dsurefire.excludedGroups= -Dgroups=live-write}.
   */
  @Test
  @Order(6)
  @Tag("live-write")
  void answersARealOnTopicQuestion() {
    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("""
            {"message": "What projects has Shafi worked on?"}
            """)
    .when()
        .post("/api/chat")
    .then()
        .statusCode(200)
        .body("success", equalTo(true))
        .body("data.reply", not(equalTo(OFF_TOPIC_REPLY)));
  }
}
