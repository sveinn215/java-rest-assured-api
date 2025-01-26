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
    private Dotenv dotenv = null;

    public BaseTest(){
        dotenv = Dotenv.configure()
        .directory("./src/test/java/com/example/apitest/resources")
        .ignoreIfMalformed()
        .ignoreIfMissing()
        .load();

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

    public void assertJsonSchema(String schemaPath, Response response){
        MatcherAssert.assertThat(
            "Validate json schema",
            response.getBody().asString(),
            JsonSchemaValidator.matchesJsonSchema(this.schemaFile)
        );
    }

    public Response invokeMethodAndAssertStatus(String method, int status){
        Response response = null;
        if(method.equals("GET")){
            response = (Response) this.request.
                get(this.apiUrl).
                then().
                statusCode(status).
                log().all().and().
                extract().
                response();

        } else if(method.equals("DELETE")){
            response = (Response) this.request.
                delete(this.apiUrl).
                then().
                statusCode(status).
                log().all().and().
                extract().
                response();
        }
        response.getBody().prettyPrint();
        return response;
    }

    public Response invokeMethodAndAssertStatus(String method, int status, Object body){
        Response response = null;
        if (method.equals("POST")){
            response = (Response) this.request.
                body(body).
                post(this.apiUrl).
                then().
                statusCode(status).
                log().all().and().
                extract().
                response();
        }else if (method.equals("PUT")){
            response = (Response) this.request.
                body(body).
                put(this.apiUrl).
                then().
                statusCode(status).
                log().all().and().
                extract().
                response();
        }
        response.getBody().prettyPrint();
        return response;
    }

    public void setApiUrl(String endpoint){
        this.apiUrl = dotenv.get("API_URL") + endpoint;
    }

    public void setApiSchema(String fileName){
        this.schemaFile = new File(System.getProperty("user.dir")+"/src/test/java/com/example/apitest/resources/schema/"+fileName);
    }
    
    public void assertJsonSchema(Response response){
        MatcherAssert.assertThat(
            "Validate json schema",
            response.getBody().asString(),
            JsonSchemaValidator.matchesJsonSchema(this.schemaFile)
        );
    }

}
