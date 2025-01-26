package com.example.apitest.java.api;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.example.apitest.BaseTest;

import io.restassured.response.Response;

public class PostCreateNewUserTest extends BaseTest {
     private HashMap<String, String> userRequestBody = new HashMap<>();
    
    @BeforeTest
    public void testSetup(){
        Instant instant = Instant.now();
        long timeStampMillis = instant.toEpochMilli();
                
        SetApiSchema("GetUserSchema.json");
        SetApiUrl("users");
        this.userRequestBody.put("name", "imam "+ timeStampMillis);
        this.userRequestBody.put("email", "imam."+timeStampMillis+"@test.com");
        this.userRequestBody.put("gender", "male");
        this.userRequestBody.put("status", "active");
    }

    @Test(description = "client succeed create user By valid request", priority = 0)
    public void verifyCreateUserByValidRequest()
    {
        SetRequestBody(userRequestBody);
        Response response=InvokeMethodAndAssertStatus("POST", 201);
        AssertJsonSchema(response);
        Assert.assertNotNull(response.getBody().jsonPath().getString("id")); 
        for (Map.Entry<String,String> fieldEntry : userRequestBody.entrySet()) {
            Assert.assertEquals(fieldEntry.getValue(), response.getBody().jsonPath().getString(fieldEntry.getKey()));
        }         
    }

    @Test(description = "client failed create user with email null", priority = 1)
    public void verifyCreateUserWithNullEmail()
    {
        this.userRequestBody.put("email", null);
        SetRequestBody(userRequestBody);
        Response response=InvokeMethodAndAssertStatus("POST", 422);
        Assert.assertEquals("can't be blank", response.getBody().jsonPath().getString("message[0]"));         
    }

    @Test(description = "client failed create user with email registered", priority = 1)
    public void verifyCreateUserWithRegisteredEmail()
    {
        this.userRequestBody.put("email", "imam@test.com");
        SetRequestBody(userRequestBody);
        Response response=InvokeMethodAndAssertStatus("POST", 422);
        Assert.assertEquals("has already been taken", response.getBody().jsonPath().getString("message[0]"));         
    }

    @Test(description = "client failed create user with email invalid format", priority = 1)
    public void verifyCreateUserWithNullEmailInvalidFormat()
    {
        String[]  invalidEmailFormat = new String[]{
            "imam.test",
            "imam.test@",
            "@test.com"
        };
        for (String format : invalidEmailFormat) {
            this.userRequestBody.put("email", format);
            System.out.println(format);
            SetRequestBody(userRequestBody);
            Response response=InvokeMethodAndAssertStatus("POST", 422);
            Assert.assertEquals("is invalid", response.getBody().jsonPath().getString("message[0]"));   
        }
              
    }

    @Test(description = "client failed create user with name null", priority = 1)
    public void verifyCreateUserWithNullName()
    {
        this.userRequestBody.put("name", null);
        SetRequestBody(userRequestBody);
        Response response=InvokeMethodAndAssertStatus("POST", 422);
        Assert.assertEquals("can't be blank", response.getBody().jsonPath().getString("message[0]"));         
    }

    @Test(description = "client failed create user with gender null", priority = 1)
    public void verifyCreateUserWithNullGender()
    {
        this.userRequestBody.put("gender", null);
        SetRequestBody(userRequestBody);
        Response response=InvokeMethodAndAssertStatus("POST", 422);
        Assert.assertEquals("can't be blank, can be male of female", response.getBody().jsonPath().getString("message[0]"));         
    }

    @Test(description = "client failed create user with gender invalid", priority = 1)
    public void verifyCreateUserWithInvalidGender()
    {
        this.userRequestBody.put("gender", "random");
        SetRequestBody(userRequestBody);
        Response response=InvokeMethodAndAssertStatus("POST", 422);
        Assert.assertEquals("can't be blank, can be male of female", response.getBody().jsonPath().getString("message[0]"));         
    }

    @Test(description = "client failed create user with status null", priority = 1)
    public void verifyCreateUserWithNullStatus()
    {
        SetRequestBody(userRequestBody);
        Response response=InvokeMethodAndAssertStatus("POST", 422);
        Assert.assertEquals("can't be blank", response.getBody().jsonPath().getString("message[0]"));         
    }

    @Test(description = "client failed create user with invalid null", priority = 1)
    public void verifyCreateUserWithInvalidStatus()
    {
        this.userRequestBody.put("status", "random");
        SetRequestBody(userRequestBody);
        Response response=InvokeMethodAndAssertStatus("POST", 422);
        Assert.assertEquals("can't be blank", response.getBody().jsonPath().getString("message[0]"));         
    }

    @Test(description = "client failed create user with invalid request body", priority = 1)
    public void verifyCreateUserWithInvalidRequestBody()
    {
        SetRequestBody("invalidRequestBody");
        InvokeMethodAndAssertStatus("POST", 406);         
    }
}
