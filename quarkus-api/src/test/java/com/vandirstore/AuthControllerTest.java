package com.vandirstore;

import com.vandirstore.dto.AuthRequestDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class AuthControllerTest {

    @Test
    public void testLoginWithInvalidCredentialsReturns401() {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setEmail("admin@vandir.com");
        request.setPassword("wrongpassword");

        RestAssured.given()
          .contentType(ContentType.JSON)
          .body(request)
          .when().post("/api/auth/login")
          .then()
             .statusCode(401);
    }
}
