package com.vandirstore;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class SystemConfigControllerTest {

    @Test
    public void testGetConfigUnauthorizedWithoutToken() {
        RestAssured.given()
          .when().get("/api/config")
          .then()
             .statusCode(401);
    }
}