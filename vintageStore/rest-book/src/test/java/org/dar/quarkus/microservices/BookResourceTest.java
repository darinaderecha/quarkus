package org.dar.quarkus.microservices;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

@QuarkusTest

class BookResourceTest {
    @Test
    public void shouldCreateABook() {
        given()
                .formParam("title", "Mermaid")
                .formParam("author", "Andersen")
                .formParam("year", "1600")
                .formParam("genre", "tale")
                .when()
                .post("http://localhost:8702/api/books")
                .then()
                .statusCode(201)
                .body("isbn13", startsWith("13-"))
                .body("title", is("Mermaid"))
                .body("author", is("Andersen"))
                .body("yearOfPublication", is(1600))
                .body("genre", is("tale"))
                .body("creationDate", startsWith("20"));
    }

}