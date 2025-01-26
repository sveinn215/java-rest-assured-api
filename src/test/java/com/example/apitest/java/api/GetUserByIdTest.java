package com.example.apitest.java.api;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.example.apitest.BaseTest;

import io.restassured.response.Response;

public class GetUserByIdTest extends BaseTest {
    @BeforeTest
    public void testSetup(){
        SetApiSchema("GetUserSchema.json");
    }

    @Test(description = "client succeed get user By valid Id", priority = 1)
    public void verifyUserByValidId()
    {
        SetApiUrl("users/7657509");
        Response response=InvokeMethodAndAssertStatus("GET", 200);
        AssertJsonSchema(response);        
    }

    @Test(description = "client failed get user By unregistered Id", priority = 1)
    public void verifyUserByUnregisteredId()
    {
        SetApiUrl("users/123456");
        Response response=InvokeMethodAndAssertStatus("GET", 404);  
        Assert.assertEquals("Resource not found", response.getBody().jsonPath().getString("message"));      
    }
}
