package com.example.apitest.java.api;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.example.apitest.BaseTest;

import io.restassured.response.Response;

public class PostUserTest extends BaseTest {
     private HashMap<String, String> userRequestBody = new HashMap<>();
     private Instant instant = Instant.now();
     private long timeStampMillis = instant.toEpochMilli();
    
    @BeforeMethod
    public void testSetup(){
        setApiSchema("GetUserSchema.json");
        setApiUrl("users");
    }

    @Test(description = "client succeed create user By valid request")
    public void verifyCreateUserByValidRequest()
    {
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "imam "+ timeStampMillis);
        requestBody.put("email", "imam."+timeStampMillis+"@test.com");
        requestBody.put("gender", "male");
        requestBody.put("status", "active");
        
        Response response=invokeMethodAndAssertStatus("POST", 201,requestBody);
        assertJsonSchema(response);
        Assert.assertNotNull(response.getBody().jsonPath().getString("id")); 
        for (Map.Entry<String,String> fieldEntry : userRequestBody.entrySet()) {
            Assert.assertEquals(fieldEntry.getValue(), response.getBody().jsonPath().getString(fieldEntry.getKey()));
        }         
    }

    @Test(description = "client failed create user with email null")
    public void verifyCreateUserWithNullEmail()
    {
        
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "imam "+ timeStampMillis);
        requestBody.put("email",null);
        requestBody.put("gender", "male");
        requestBody.put("status", "active");

        Response response=invokeMethodAndAssertStatus("POST", 422, requestBody);
        Assert.assertEquals("can't be blank", response.getBody().jsonPath().getString("message[0]"));         
    }

    @Test(description = "client failed create user with email registered")
    public void verifyCreateUserWithRegisteredEmail()
    {
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "imam "+ timeStampMillis);
        requestBody.put("gender", "male");
        requestBody.put("status", "active");
        requestBody.put("email", "imam@test.com");

        Response response=invokeMethodAndAssertStatus("POST", 422, requestBody);
        Assert.assertEquals("has already been taken", response.getBody().jsonPath().getString("message[0]"));         
    }

    @Test(description = "client failed create user with email invalid format")
    public void verifyCreateUserWithNullEmailInvalidFormat()
    {
        String[]  invalidEmailFormat = new String[]{
            "imam.test",
            "imam.test@",
            "@test.com"
        };
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "imam "+ timeStampMillis);
        requestBody.put("gender", "male");
        requestBody.put("status", "active");

        for (String format : invalidEmailFormat) {
            requestBody.put("email", format);
            Response response=invokeMethodAndAssertStatus("POST", 422, requestBody);
            Assert.assertEquals("is invalid", response.getBody().jsonPath().getString("message[0]"));   
        }
              
    }

    @Test(description = "client failed create user with name null")
    public void verifyCreateUserWithNullName()
    {
        
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("name", null);
        requestBody.put("email", "imam."+timeStampMillis+"@test.com");
        requestBody.put("gender", "male");
        requestBody.put("status", "active");

        Response response=invokeMethodAndAssertStatus("POST", 422, requestBody);
        Assert.assertEquals("can't be blank", response.getBody().jsonPath().getString("message[0]"));         
    }

    @Test(description = "client failed create user with gender null")
    public void verifyCreateUserWithNullGender()
    {
        
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "imam "+ timeStampMillis);
        requestBody.put("email", "imam."+timeStampMillis+"@test.com");
        requestBody.put("gender", null);
        requestBody.put("status", "active");

        Response response=invokeMethodAndAssertStatus("POST", 422, requestBody);
        Assert.assertEquals("can't be blank, can be male of female", response.getBody().jsonPath().getString("message[0]"));         
    }

    @Test(description = "client failed create user with gender invalid")
    public void verifyCreateUserWithInvalidGender()
    {
        
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "imam "+ timeStampMillis);
        requestBody.put("email", "imam."+timeStampMillis+"@test.com");
        requestBody.put("gender", "random");
        requestBody.put("status", "active");
        Response response=invokeMethodAndAssertStatus("POST", 422, requestBody);
        Assert.assertEquals("can't be blank, can be male of female", response.getBody().jsonPath().getString("message[0]"));         
    }

    @Test(description = "client failed create user with status null")
    public void verifyCreateUserWithNullStatus()
    {
        
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "imam "+ timeStampMillis);
        requestBody.put("email", "imam."+timeStampMillis+"@test.com");
        requestBody.put("gender", "male");
        requestBody.put("status", null);

        Response response=invokeMethodAndAssertStatus("POST", 422, requestBody);
        Assert.assertEquals("can't be blank", response.getBody().jsonPath().getString("message[0]"));         
    }

    @Test(description = "client failed create user with invalid status")
    public void verifyCreateUserWithInvalidStatus()
    {
        
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "imam "+ timeStampMillis);
        requestBody.put("email", "imam."+timeStampMillis+"@test.com");
        requestBody.put("gender", "male");
        requestBody.put("status", "random");

        Response response=invokeMethodAndAssertStatus("POST", 422, requestBody);
        Assert.assertEquals("can't be blank", response.getBody().jsonPath().getString("message[0]"));         
    }
}
