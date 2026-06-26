package apiautomation.restfulbooker.RestUtility;

import static org.hamcrest.Matchers.lessThan;

import java.util.HashMap;

import apiautomation.restfulbooker.constants.Constants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RestUtil {

	public static Response postRequest(String baseuri,String basepath, HashMap<String,Object> headers, Object postReqBody,
			int statusCode, long responseTime
			) {
		
		Response response = RestAssured.given().log().all().baseUri(baseuri).contentType(ContentType.JSON)
				.headers(headers)
				.body(postReqBody)
				.when()
				.post(basepath).then().log().body().statusCode(statusCode).time(lessThan(responseTime)).extract().response();
		
		return response;
	}
	
	/* Post request + POJO + Schema Validation */
	public static Response postRequest(String baseuri,String basepath, HashMap<String,Object> headers, Object postReqBody,
			int statusCode, long responseTime, String jsonSchema
			) {
		
		Response response = RestAssured.given().log().all().baseUri(baseuri).contentType(ContentType.JSON)
				.headers(headers)
				.body(postReqBody)
				.when()
				.post(basepath).then().log().body().statusCode(statusCode).time(lessThan(responseTime))
				.body(matchesJsonSchemaInClasspath(jsonSchema)).extract().response();
		
		return response;
	}
	
	/* Post request + HashMaps + Schema Validation */
	public static Response postRequest(String baseuri,String basepath, HashMap<String,Object> headers, HashMap<String,Object> postReqBody,
			int statusCode, long responseTime, String jsonSchema
			) {
		
		Response response = RestAssured.given().log().all().baseUri(baseuri).contentType(ContentType.JSON)
				.headers(headers)
				.body(postReqBody)
				.when()
				.post(basepath).then().log().body().statusCode(statusCode).time(lessThan(responseTime))
				.body(matchesJsonSchemaInClasspath(jsonSchema)).extract().response();
		
		return response;
	}
	
	public static Response getRequest(String baseuri,String basepath, HashMap<String,Object> headers, 
			int statusCode, long responseTime
			) {
		
		Response response = RestAssured.given().log().all().baseUri(baseuri).contentType(ContentType.JSON)
				.headers(headers)
				.when()
				.get(basepath).then().log().body().statusCode(statusCode).time(lessThan(responseTime)).extract().response();
		
		return response;
	}
	
	public static Response patchRequest(String baseuri,String basepath, HashMap<String,Object> headers, Object patchReqBody,
			int statusCode, long responseTime
			) {
		
		Response response = RestAssured.given().log().all().baseUri(baseuri).contentType(ContentType.JSON)
				.headers(headers)
				.body(patchReqBody)
				.when()
				.patch(basepath).then().log().body().statusCode(statusCode).time(lessThan(responseTime)).extract().response();
		
		return response;
	}
	
	
	public static Response putRequest(String baseuri,String basepath, HashMap<String,Object> headers, Object patchReqBody,
			int statusCode, long responseTime
			) {
		
		Response response = RestAssured.given().log().all().baseUri(baseuri).contentType(ContentType.JSON)
				.headers(headers)
				.body(patchReqBody)
				.when()
				.patch(basepath).then().log().body().statusCode(statusCode).time(lessThan(responseTime)).extract().response();
		
		return response;
	}
	
	public static Response deleteRequest(String baseuri,String basepath, HashMap<String,Object> headers, Object patchReqBody,
			int statusCode, long responseTime
			) {
		
		Response response = RestAssured.given().log().all().baseUri(baseuri).contentType(ContentType.JSON)
				.headers(headers)
				.body(patchReqBody)
				.when()
				.patch(basepath).then().log().body().statusCode(statusCode).time(lessThan(responseTime)).extract().response();
		
		return response;
	}


}
