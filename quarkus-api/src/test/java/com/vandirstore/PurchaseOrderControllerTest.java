package com.vandirstore;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class PurchaseOrderControllerTest {

    @Test
    public void testGetOrdersUnauthorizedWithoutToken() {
        RestAssured.given()
          .when().get("/api/orders")
          .then()
             .statusCode(401);
    }
}