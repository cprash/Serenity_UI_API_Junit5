package com.epam.junit.serenity.APITests;

import io.restassured.response.Response;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SerenityJUnit5Extension.class)
public class PetStoreApiSerenityTest {

    private static final String BASE_URL = "https://petstore.swagger.io/v2/pet/findByStatus?status=available";

    @Test
    void shouldReturnStatusCode200() {
        SerenityRest
                .given()
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(200);
    }

    @Test
    void shouldReturnAvailablePetsAndCountGreaterThan5() {
        Response response = SerenityRest
                .given()
                .when()
                .get(BASE_URL);

        response.then().statusCode(200);

        var pets = response.jsonPath().getList("$");
        assertThat("Number of pets returned", pets.size(), greaterThan(5));
        System.out.println("Number of pets returned: " + pets.size());

        for (int i = 0; i < pets.size(); i++) {
            String status = response.jsonPath().getString("[" + i + "].status");
            assertThat("Pet at index " + i + " has status " + status, status, equalTo("available"));
        }
    }

    @Test
    void shouldHaveCorrectHeaders() {
        Response response = SerenityRest
                .given()
                .when()
                .get(BASE_URL);

        response.then().statusCode(200);

        String contentType = response.getHeader("Content-Type");
        assertThat(contentType, containsString("application/json"));

        String allowMethods = response.getHeader("Access-Control-Allow-Methods");
        assertThat(allowMethods, notNullValue());
        assertThat(allowMethods, containsString("GET"));
    }

    @Test
    void shouldReturn404ForInvalidEndpoint() {
        SerenityRest
                .given()
                .when()
                .get("https://petstore.swagger.io/v2/pet/findByStatusINVALID?status=available")
                .then()
                .statusCode(404);
    }
}
