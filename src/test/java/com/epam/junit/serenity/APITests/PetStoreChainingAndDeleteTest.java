package com.epam.junit.serenity.APITests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;

import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class PetStoreChainingAndDeleteTest {

        RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri("https://petstore.swagger.io")
                .setBasePath("/v2")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON).build();

    @Test
    void testAllMethods(){
        Map<String,Object> catMap=new HashMap<>();
        catMap.put("id",1);
        catMap.put("name","dog");
        Map<String,Object> petMap=new HashMap<>();
        petMap.put("name","doggie-epam");
        petMap.put("status","pending");
        petMap.put("category",catMap);

        String newPetid = SerenityRest.given().spec(requestSpecification).body(petMap)
                .when().post("/pet")
                .path("id").toString();

        SerenityRest.given().spec(requestSpecification).pathParam("newPetid",newPetid)
                .when().get("/pet/{newPetid}")
                .then().statusCode(200)
                .and().body("status",equalTo("pending"));

        petMap.put("status","available");
        petMap.put("id",newPetid);

        SerenityRest.given().spec(requestSpecification)
                .body(petMap)
                .when().put("/pet").then().statusCode(200);

        SerenityRest.given().spec(requestSpecification).pathParam("newPetid",newPetid)
                .when().get("/pet/{newPetid}")
                .then().statusCode(200)
                .and().body("status",equalTo("available"));

        SerenityRest.given().spec(requestSpecification).pathParams("newPetid",newPetid)
                .when().delete("/pet/{newPetid}")
                .then().statusCode(200);

        SerenityRest.given().spec(requestSpecification).pathParam("newPetid",newPetid)
                .when().get("/pet/{newPetid}")
                .then().statusCode(404);

    }
}
