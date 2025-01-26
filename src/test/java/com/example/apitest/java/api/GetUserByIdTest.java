package com.example.apitest.java.api;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.example.apitest.BaseTest;

import io.restassured.response.Response;

public class GetUserByIdTest extends BaseTest {
    @BeforeTest
    public void testSetup(){
        setApiSchema("GetUserSchema.json");
    }

    @Test(description = "client succeed get user By valid Id", priority = 1)
    public void verifyUserByValidId()
    {
        setApiUrl("users/7657582");
        Response response=invokeMethodAndAssertStatus("GET", 200);
        assertJsonSchema(response);        
    }

    @Test(description = "client failed get user By unregistered Id", priority = 1)
    public void verifyUserByUnregisteredId()
    {
        setApiUrl("users/123456");
        Response response=invokeMethodAndAssertStatus("GET", 404);  
        Assert.assertEquals("Resource not found", response.getBody().jsonPath().getString("message"));      
    }
}
