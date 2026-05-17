package com.vandirstore;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ProductControllerTest {

    @Test
    public void testGetProductsWithoutTokenReturns401() {
        RestAssured.given()
          .when().get("/api/products")
          .then()
             .statusCode(401); // Unauthorized
    }
}
