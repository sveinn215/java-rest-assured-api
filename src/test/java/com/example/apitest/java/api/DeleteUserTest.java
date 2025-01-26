package com.example.apitest.java.api;

import java.time.Instant;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.example.apitest.BaseTest;

import io.restassured.response.Response;

public class DeleteUserTest extends BaseTest{
    @Test(description = "client succeed delete By valid Id", priority = 1)
    public void verifyDeleteWithValidId()
    {
        Instant instant = Instant.now();
        long timeStampMillis = instant.toEpochMilli();
        HashMap<String, String> requestBody = new HashMap<>();

        setApiUrl("users");
        requestBody.put("name", "imam "+ timeStampMillis);
        requestBody.put("email", "imam."+timeStampMillis+"@test.com");
        requestBody.put("gender", "male");
        requestBody.put("status", "active");
        
        Response response = invokeMethodAndAssertStatus("POST", 201,requestBody);

        setApiUrl("users/"+response.getBody().jsonPath().getString("id"));
        response = invokeMethodAndAssertStatus("GET", 200);     
    }

    @Test(description = "client failed delete user By unregistered Id", priority = 1)
    public void verifyDeleteWithUnregisteredId()
    {
        setApiUrl("users/123456");
        Response response=invokeMethodAndAssertStatus("DELETE", 404);  
        Assert.assertEquals("Resource not found", response.getBody().jsonPath().getString("message"));      
    }

    @Test(description = "client failed delete user without Id", priority = 1)
    public void verifyDeleteWithoutId()
    {
        setApiUrl("users");
        invokeMethodAndAssertStatus("DELETE", 404);  
    }
        
}
