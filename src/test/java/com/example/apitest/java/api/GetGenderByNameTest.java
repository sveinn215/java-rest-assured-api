package com.example.apitest.java.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.example.apitest.BaseTest;

import io.restassured.response.Response;

public class GetGenderByNameTest extends BaseTest
{
   private String apiEndpoint = "https://api.genderize.io/";


    @Test(description = "To verify gender based on their name", priority = 1)
    public void verifyGenderBasedOnTheirName()
    {
        Map<String,String> names = new HashMap<String,String>();
        String schemaPath = System.getProperty("user.dir")+"/src/test/java/com/example/apitest/resources/schema/GetGenderSchema.json";
        names.put("Isabella", "female");
        names.put("Olivia", "female");
        names.put("Ethan", "male");
        names.put("Christopher", "male");
        names.put("Sophia", "female");

        for (Map.Entry<String,String> namEntry : names.entrySet()) {
            Response response=InvokeGetMethodAndAssert(apiEndpoint, Collections.singletonMap("name", namEntry.getKey()), 200);
            Assert.assertEquals(namEntry.getValue(), response.getBody().jsonPath().getString("gender"));
            AssertJsonSchema(schemaPath, response);
        }        
    }

    @Test(description = "To verify gender with invalid name", priority = 2)
    public void verifyGenderWithInvalidName()
    {
        Response response=InvokeGetMethodAndAssert(apiEndpoint, Collections.singletonMap("name", "123456"), 200); 
        Assert.assertEquals(null, response.getBody().jsonPath().getString("gender"));
        Assert.assertEquals(0, response.getBody().jsonPath().getInt("count"));
        Assert.assertEquals(0.0, response.getBody().jsonPath().getDouble("probability"));       
    }

    @Test(description = "To verify gender with empty name", priority = 2)
    public void verifyGenderWithEmptyName()
    {
        Response response=InvokeGetMethodAndAssert(apiEndpoint, Collections.singletonMap("name", ""), 200);
        Assert.assertEquals("", response.getBody().jsonPath().getString("name")); 
        Assert.assertEquals(null, response.getBody().jsonPath().getString("gender"));
        Assert.assertEquals(0, response.getBody().jsonPath().getInt("count"));
        Assert.assertEquals(0.0, response.getBody().jsonPath().getDouble("probability"));       
    }

    @Test(description = "To verify gender with null name", priority = 2)
    public void verifyGenderWithNullName()
    {
        InvokeGetMethodAndAssert(apiEndpoint, null, 422);  
    }
}
