package com.example.apitest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.MatcherAssert;

import io.github.cdimascio.dotenv.Dotenv;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseTest {
    private Map<String,String> defaultHeaders = new HashMap<>();
    private RequestSpecification request = RestAssured.given();
    private String apiUrl = null;
    private File schemaFile = null;

    public BaseTest(){
        Dotenv dotenv = Dotenv.configure()
        .directory("./src/test/java/com/example/apitest/resources")
        .ignoreIfMalformed()
        .ignoreIfMissing()
        .load();

        this.apiUrl = dotenv.get("API_URL");
        defaultHeaders.put("Accept", "application/json, text/plain, */*");
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("Authorization", dotenv.get("BEARER_TOKEN"));
        request = RestAssured.
            given().
            headers(this.defaultHeaders).
            when();
    }

    public Response InvokeGetMethodAndAssert(String url,Map<String, String> map,int statusCode){
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
            JsonSchemaValidator.matchesJsonSchema(this.schemaFile)
        );
    }

    public Response InvokeMethodAndAssertStatus(String method, int status){
        Response response = null;
        if(method.equals("GET")){
            response = (Response) this.request.
                get(this.apiUrl).
                then().
                statusCode(status).
                extract().
                response();
        }else if (method.equals("POST")){
            response = (Response) this.request.
                post(this.apiUrl).
                then().
                statusCode(status).
                extract().
                response();
        }
        response.getBody().prettyPrint();
        return response;
    }

    public void SetApiUrl(String endpoint){
        this.apiUrl = this.apiUrl + endpoint;
    }

    public void SetApiSchema(String fileName){
        this.schemaFile = new File(System.getProperty("user.dir")+"/src/test/java/com/example/apitest/resources/schema/"+fileName);
    }

    public void SetRequestBody(Object requestBody){
        this.request.body(requestBody);
    }
    
    public void AssertJsonSchema(Response response){
        MatcherAssert.assertThat(
            "Validate json schema",
            response.getBody().asString(),
            JsonSchemaValidator.matchesJsonSchema(this.schemaFile)
        );
    }
}
