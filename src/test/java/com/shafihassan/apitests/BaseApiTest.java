package com.shafihassan.apitests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

/**
 * Base class for API test suites. Configures REST Assured's base URI once, so
 * individual test classes only need to reference relative paths.
 */
public abstract class BaseApiTest {

  private static final String DEFAULT_BASE_URI = "https://shafihassan.com";

  @BeforeAll
  static void configureRestAssured() {
    RestAssured.baseURI = System.getProperty("api.baseUri", DEFAULT_BASE_URI);
  }
}
