package com.vandirstore;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ProviderControllerTest {

    @Test
    public void testGetProvidersUnauthorizedWithoutToken() {
        RestAssured.given()
          .when().get("/api/providers")
          .then()
             .statusCode(401);
    }
}