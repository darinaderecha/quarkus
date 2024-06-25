package org.dar.quarkus.microservices;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;

@QuarkusTest
class NumberResourceTest {
    @Test
    void testNumberEndpoint() {
        given()
                .when().get("/api/numbers")
                .then()
                .statusCode(200)
                .body("isbn13", startsWith("13-"))
                .body("isbn10", startsWith("10-"))
                .body(not(hasKey("generationDate")));
    }

}