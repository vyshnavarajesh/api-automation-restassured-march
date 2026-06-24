package apiautomation.restfulbooker.utils;

import static org.hamcrest.Matchers.lessThan;

import org.json.JSONObject;

import apiautomation.restfulbooker.constants.Constants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class TokenGenUtil {
	
	public static String tokenGenerate() {
		
		JSONObject data = new JSONObject(); // org.json 
		data.put("username", "admin");
		data.put("password","password123");
		
		Response res = RestAssured
		.given().log().all().baseUri(Constants.BaseURI)
		.contentType(ContentType.JSON)
		.body(data.toString())
		.when()
		.post(Constants.AuthorizationEndPoint)
		.then().log().body()
		.statusCode(200)
		.time(lessThan(50000L)).extract().response();
		
		return res.jsonPath().getString("token");
		
	}

}
