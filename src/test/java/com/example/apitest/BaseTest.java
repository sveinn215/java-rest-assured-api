package com.example.apitest;
import java.io.File;
import java.util.Map;

import org.hamcrest.MatcherAssert;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseTest {
    public Response InvokeGetMethodAndAssert(String url,Map<String, String> map,int statusCode){
        RequestSpecification request = RestAssured.given();

        if(map != null){
            request.params(map);
        }

        Response response = request.when()
                                    .get(url)
                                    .then()
                                    .contentType("application/json")
                                    .statusCode(statusCode)
                                    .extract()
                                    .response();
                                    
        response.getBody().prettyPrint();
        return response;
    }

    public void AssertJsonSchema(String schemaPath, Response response){
        MatcherAssert.assertThat(
            "Validate json schema",
            response.getBody().asString(),
            JsonSchemaValidator.matchesJsonSchema(new File(schemaPath))
        );
    }
}
