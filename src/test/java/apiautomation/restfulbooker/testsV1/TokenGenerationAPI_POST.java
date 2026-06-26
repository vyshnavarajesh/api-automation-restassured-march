package apiautomation.restfulbooker.testsV1;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

import org.json.JSONObject;
import org.testng.annotations.Test;

import apiautomation.restfulbooker.constants.Constants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class TokenGenerationAPI_POST {
	
	@Test
	public void tokenGeneration()
	{
		JSONObject data = new JSONObject(); // org.json 
		data.put("username", "admin");
		data.put("password","password123");
		
		RestAssured
		.given().log().all().baseUri(Constants.BaseURI)
		.contentType(ContentType.JSON)
		.body(data.toString())
		.when()
		.post("/auth")
		.then().log().body()
		.statusCode(200)
		.time(lessThan(50000L)); // responsetime - lessThan is coming from hamcrest dependency
	}
	
	@Test
	public void tokenGenerationInvalidTest()
	{
		JSONObject data = new JSONObject(); // org.json 
		data.put("username", "admin123");
		data.put("password","password123");
		
		RestAssured
		.given().log().all().baseUri(Constants.BaseURI)
		.contentType(ContentType.JSON)
		.body(data.toString())
		.when()
		.post("/auth")
		.then().log().body()
		.statusCode(400)
		.time(lessThan(50000L)); // responsetime - lessThan is coming from hamcrest dependency
	}

}
