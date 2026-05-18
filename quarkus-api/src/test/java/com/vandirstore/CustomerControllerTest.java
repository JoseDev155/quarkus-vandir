package com.vandirstore;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class CustomerControllerTest {

    @Test
    public void testGetCustomersUnauthorizedWithoutToken() {
        RestAssured.given()
          .when().get("/api/customers")
          .then()
             .statusCode(401);
    }
}