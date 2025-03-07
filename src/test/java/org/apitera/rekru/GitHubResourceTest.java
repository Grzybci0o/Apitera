package org.apitera.rekru;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class GitHubResourceTest {

    @Test
    public void testGetUserRepositories() {
        given()
                .when().get("/github/repos/Grzybci0o")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body("size()", greaterThan(0))
                .body("[0].repositoryName", not(emptyString()))
                .body("[0].ownerLogin", not(emptyString()))
                .body("[0].branches.size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetUserRepositoriesNotFound() {
        given()
                .when().get("/github/repos/nonexistentuser12345")
                .then()
                .statusCode(404)
                .contentType(MediaType.APPLICATION_JSON)
                .body("status", equalTo(404))
                .body("message", equalTo("Error fetching repositories - Not Found, status code 404"));
    }
}
