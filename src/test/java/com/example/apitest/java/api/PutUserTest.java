package com.example.apitest.java.api;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.example.apitest.BaseTest;

import io.restassured.response.Response;

public class PutUserTest extends BaseTest {
    private HashMap<String, String> userRequestBody = new HashMap<>();
    private Instant instant = Instant.now();
    private long timeStampMillis = instant.toEpochMilli();
   
   @BeforeMethod
   public void testSetup(){
       setApiSchema("GetUserSchema.json");
       setApiUrl("users/7657582");
   }

   @Test(description = "client succeed update user By valid request")
   public void verifyUpdateUserByValidRequest()
   {
       HashMap<String, String> requestBody = new HashMap<>();
       requestBody.put("name", "imam "+ timeStampMillis);
       requestBody.put("email", "imam."+timeStampMillis+"@test.com");
       requestBody.put("gender", "male");
       requestBody.put("status", "active");
       
       Response response=invokeMethodAndAssertStatus("PUT", 200,requestBody);
       assertJsonSchema(response);
       Assert.assertNotNull(response.getBody().jsonPath().getString("id")); 
       for (Map.Entry<String,String> fieldEntry : userRequestBody.entrySet()) {
           Assert.assertEquals(fieldEntry.getValue(), response.getBody().jsonPath().getString(fieldEntry.getKey()));
       }         
   }

   @Test(description = "client failed update user with email null")
   public void verifyUpdateUserWithNullEmail()
   {
       
       HashMap<String, String> requestBody = new HashMap<>();
       requestBody.put("name", "imam "+ timeStampMillis);
       requestBody.put("email",null);
       requestBody.put("gender", "male");
       requestBody.put("status", "active");

       Response response=invokeMethodAndAssertStatus("PUT", 422, requestBody);
       Assert.assertEquals("can't be blank", response.getBody().jsonPath().getString("message[0]"));         
   }

   @Test(description = "client failed update user with email registered")
   public void verifyUpdateUserWithRegisteredEmail()
   {
       HashMap<String, String> requestBody = new HashMap<>();
       requestBody.put("name", "imam "+ timeStampMillis);
       requestBody.put("gender", "male");
       requestBody.put("status", "active");
       requestBody.put("email", "imam@test.com");

       Response response=invokeMethodAndAssertStatus("PUT", 422, requestBody);
       Assert.assertEquals("has already been taken", response.getBody().jsonPath().getString("message[0]"));         
   }

   @Test(description = "client failed update user with email invalid format")
   public void verifyUpdateUserWithNullEmailInvalidFormat()
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
           Response response=invokeMethodAndAssertStatus("PUT", 422, requestBody);
           Assert.assertEquals("is invalid", response.getBody().jsonPath().getString("message[0]"));   
       }
             
   }

   @Test(description = "client failed update user with name null")
   public void verifyUpdateUserWithNullName()
   {
       
       HashMap<String, String> requestBody = new HashMap<>();
       requestBody.put("name", null);
       requestBody.put("email", "imam."+timeStampMillis+"@test.com");
       requestBody.put("gender", "male");
       requestBody.put("status", "active");

       Response response=invokeMethodAndAssertStatus("PUT", 422, requestBody);
       Assert.assertEquals("can't be blank", response.getBody().jsonPath().getString("message[0]"));         
   }

   @Test(description = "client failed update user with gender null")
   public void verifyUpdateUserWithNullGender()
   {
       
       HashMap<String, String> requestBody = new HashMap<>();
       requestBody.put("name", "imam "+ timeStampMillis);
       requestBody.put("email", "imam."+timeStampMillis+"@test.com");
       requestBody.put("gender", null);
       requestBody.put("status", "active");

       Response response=invokeMethodAndAssertStatus("PUT", 422, requestBody);
       Assert.assertEquals("can't be blank, can be male of female", response.getBody().jsonPath().getString("message[0]"));         
   }

   @Test(description = "client failed update user with gender invalid")
   public void verifyUpdateUserWithInvalidGender()
   {
       
       HashMap<String, String> requestBody = new HashMap<>();
       requestBody.put("name", "imam "+ timeStampMillis);
       requestBody.put("email", "imam."+timeStampMillis+"@test.com");
       requestBody.put("gender", "random");
       requestBody.put("status", "active");
       Response response=invokeMethodAndAssertStatus("PUT", 422, requestBody);
       Assert.assertEquals("can't be blank, can be male of female", response.getBody().jsonPath().getString("message[0]"));         
   }

   @Test(description = "client failed update user with status null")
   public void verifyUpdateUserWithNullStatus()
   {
       
       HashMap<String, String> requestBody = new HashMap<>();
       requestBody.put("name", "imam "+ timeStampMillis);
       requestBody.put("email", "imam."+timeStampMillis+"@test.com");
       requestBody.put("gender", "male");
       requestBody.put("status", null);

       Response response=invokeMethodAndAssertStatus("PUT", 422, requestBody);
       Assert.assertEquals("can't be blank", response.getBody().jsonPath().getString("message[0]"));         
   }

   @Test(description = "client failed update user with invalid status")
   public void verifyUpdateUserWithInvalidStatus()
   {
       
       HashMap<String, String> requestBody = new HashMap<>();
       requestBody.put("name", "imam "+ timeStampMillis);
       requestBody.put("email", "imam."+timeStampMillis+"@test.com");
       requestBody.put("gender", "male");
       requestBody.put("status", "random");

       Response response=invokeMethodAndAssertStatus("PUT", 422, requestBody);
       Assert.assertEquals("can't be blank", response.getBody().jsonPath().getString("message[0]"));         
   }

   @Test(description = "client failed update user with invalid userId")
   public void verifyUpdateUserWithInvalidUserId()
   {
       
       HashMap<String, String> requestBody = new HashMap<>();
       requestBody.put("name", "imam "+ timeStampMillis);
       requestBody.put("email", "imam."+timeStampMillis+"@test.com");
       requestBody.put("gender", "male");
       requestBody.put("status", "active");

       setApiUrl("users/12356");

       Response response=invokeMethodAndAssertStatus("PUT", 404, requestBody);
       Assert.assertEquals("R", response.getBody().jsonPath().getString("message[0]"));         
   }
}
