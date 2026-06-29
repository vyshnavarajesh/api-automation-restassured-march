package apiautomation.restfulbooker.RestUtility;

import static org.hamcrest.Matchers.lessThan;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import apiautomation.restfulbooker.constants.Constants;
import apiautomation.restfulbooker.utils.PrettyPrintJSONUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RestUtil {

	private static final Logger logger = LogManager.getLogger(RestUtil.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Helper method to serialize object to JSON string for logging
	 */
	private static String serializeToJson(Object obj) {
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (Exception e) {
			return obj.toString();
		}
	}

	/**
	 * POST Request with status code and response time validation
	 */
	public static Response postRequest(String baseuri, String basepath, HashMap<String, Object> headers,
			Object postReqBody, int statusCode, long responseTime) {

		logger.debug("########### POST REQUEST STARTED ###########");
		logger.debug("Request Headers: {}", headers);
		logger.debug("Request Body (JSON):\n{}", serializeToJson(postReqBody));

// Send request and get the response object
		Response response = RestAssured.given().log().all() // optional: built-in console logging
				.baseUri(baseuri).contentType(ContentType.JSON).headers(headers).body(postReqBody).when()
				.post(basepath);


		try {
			String responseBody = response.getBody().asString();
			logger.info("Response Status Code: {}", response.getStatusCode());
			logger.info("Response Time: {} ms", response.getTime());
			logger.info("Response Content-Type: {}", response.getContentType());
			logger.info("Response Body Length: {}", responseBody.length());
			logger.info("Response Body (JSON):\n{}", PrettyPrintJSONUtil.prettyPrintJson(responseBody));
		} catch (Exception e) {
			logger.error("ERROR capturing response body: ", e);
		}

		response.then().log().all() // optional: built-in logging before validation
				.statusCode(statusCode).time(lessThan(responseTime));

		logger.info("########### POST REQUEST COMPLETED ###########\n");
		return response;
	}

	/**
	 * POST Request with Schema Validation (POJO)
	 */
	public static Response postRequest(String baseuri, String basepath, HashMap<String, Object> headers,
			Object postReqBody, int statusCode, long responseTime, String jsonSchema) {

		logger.debug("########### POST REQUEST WITH SCHEMA VALIDATION STARTED###########");
		logger.debug("Base URI: {}", baseuri);
		logger.debug("Base Path: {}", basepath);
		logger.debug("Request Body (JSON):\n{}", serializeToJson(postReqBody));
		logger.debug("JSON Schema: {}", jsonSchema);

		Response response = RestAssured.given().log().all().baseUri(baseuri).contentType(ContentType.JSON)
				.headers(headers).body(postReqBody).when().post(basepath).then().log().all().statusCode(statusCode)
				.time(lessThan(responseTime)).body(matchesJsonSchemaInClasspath(jsonSchema)).extract().response();

		try {
			String responseBody = response.getBody().asString();
			logger.info("Response Status Code: {}", response.getStatusCode());
			logger.info("Response Body Length: {}", responseBody.length());
			logger.info("Response Body (JSON):\n{}", responseBody);
			logger.info("Schema Validation: PASSED");
		} catch (Exception e) {
			logger.error("ERROR capturing response body: ", e);
			logger.error("Exception message: {}", e.getMessage());
		}
		logger.info("########### POST REQUEST WITH SCHEMA VALIDATION COMPLETED ###########\n");

		return response;
	}

	/**
	 * POST Request with Schema Validation (HashMap)
	 */
	public static Response postRequest(String baseuri, String basepath, HashMap<String, Object> headers,
			HashMap<String, Object> postReqBody, int statusCode, long responseTime, String jsonSchema) {

		logger.debug("########### POST REQUEST (HASHMAP) WITH SCHEMA VALIDATION STARTED ###########");
		logger.debug("Base URI: {}", baseuri);
		logger.debug("Base Path: {}", basepath);
		logger.debug("Request Body (JSON):\n{}", serializeToJson(postReqBody));

		Response response = RestAssured.given().log().all().baseUri(baseuri).contentType(ContentType.JSON)
				.headers(headers).body(postReqBody).when().post(basepath).then().log().all().statusCode(statusCode)
				.time(lessThan(responseTime)).body(matchesJsonSchemaInClasspath(jsonSchema)).extract().response();

		try {
			String responseBody = response.getBody().asString();
			logger.info("Response Status Code: {}", response.getStatusCode());
			logger.info("Response Body Length: {}", responseBody.length());
			logger.info("Response Body (JSON):\n{}", responseBody);
		} catch (Exception e) {
			logger.error("ERROR capturing response body: ", e);
			logger.error("Exception message: {}", e.getMessage());
		}
		logger.info("########### POST REQUEST (HASHMAP) WITH SCHEMA VALIDATION COMPLETED ###########\n");

		return response;
	}

	/**
	 * GET Request
	 */
	public static Response getRequest(String baseuri, String basepath, HashMap<String, Object> headers, HashMap<String, Object>  pathParameters,int statusCode,
			long responseTime) {

		logger.debug("########### GET REQUEST STARTED ###########");
		logger.debug("Base URI: {}", baseuri);
		logger.debug("Base Path: {}", basepath);
		
		if (pathParameters != null && !pathParameters.isEmpty()) {
			logger.debug("Path Parameters: {}", pathParameters);
		}
 
		var givenRequest = RestAssured.given().log().all().baseUri(baseuri).contentType(ContentType.JSON);
		 
		if (headers != null && !headers.isEmpty()) {
			givenRequest.headers(headers);
		}
 
		if (pathParameters != null && !pathParameters.isEmpty()) {
			givenRequest.pathParams(pathParameters);
		}
 
		
		Response response = givenRequest.when().get(basepath).then().log().all().statusCode(statusCode)
				.time(lessThan(responseTime)).extract().response();

		try {
			String responseBody = response.getBody().asString();
			logger.info("Response Status Code: {}", response.getStatusCode());
			logger.info("Response Body Length: {}", responseBody.length());
			logger.info("Response Body (JSON):\n{}", responseBody);
		} catch (Exception e) {
			logger.error("ERROR capturing response body: ", e);
			logger.error("Exception message: {}", e.getMessage());
		}
		logger.info("########### GET REQUEST COMPLETED ###########\n");

		return response;
	}
	
	/**
	 * PUT Request
	 */
	public static Response putRequest(String baseuri, String basepath, HashMap<String, ?> headers,HashMap<String, ?>  pathParameters,
			Object putReqBody, int statusCode, long responseTime) {

		logger.debug("########### PUT REQUEST STARTED ###########");
		logger.debug("Base URI: {}", baseuri);
		logger.debug("Base Path: {}", basepath);
		logger.debug("Request Body (JSON):\n{}", serializeToJson(putReqBody));
		
		if (pathParameters != null && !pathParameters.isEmpty()) {
			logger.debug("Path Parameters: {}", pathParameters);
		}
 
		//Pre-condition
		var givenRequest = RestAssured.given().log().all().baseUri(baseuri).contentType(ContentType.JSON).body(putReqBody);
	
		if (headers != null && !headers.isEmpty()) {
			givenRequest.headers(headers);
		}
 
		if (pathParameters != null && !pathParameters.isEmpty()) {
			givenRequest.pathParams(pathParameters);
		}

		//post-conditon
		Response response = givenRequest.when().put(basepath).then().log().all().statusCode(statusCode)
				.time(lessThan(responseTime)).extract().response();

		try {
			String responseBody = response.getBody().asString();
			logger.info("Response Status Code: {}", response.getStatusCode());
			logger.info("Response Body Length: {}", responseBody.length());
			logger.info("Response Body (JSON):\n{}", responseBody);
		} catch (Exception e) {
			logger.error("ERROR capturing response body: ", e);
			logger.error("Exception message: {}", e.getMessage());
		}
		logger.info("########### PUT REQUEST COMPLETED ###########\n");

		return response;
	}

	/**
	 * PATCH Request
	 */
	public static Response patchRequest(String baseuri, String basepath, HashMap<String, Object> headers,HashMap<String, ?>  pathParameters,
			Object patchReqBody, int statusCode, long responseTime) {

		logger.debug("########### PATCH REQUEST STARTED ###########");
		logger.debug("Base URI: {}", baseuri);
		logger.debug("Base Path: {}", basepath);
		logger.debug("Request Body (JSON):\n{}", serializeToJson(patchReqBody));
		
		if (pathParameters != null && !pathParameters.isEmpty()) {
			logger.debug("Path Parameters: {}", pathParameters);
		}
 
		var givenRequest = RestAssured.given().log().all().baseUri(baseuri).contentType(ContentType.JSON).body(patchReqBody);
	
		if (headers != null && !headers.isEmpty()) {
			givenRequest.headers(headers);
		}
 
		if (pathParameters != null && !pathParameters.isEmpty()) {
			givenRequest.pathParams(pathParameters);
		}
		
		Response response = givenRequest.when().patch(basepath).then().log().all().statusCode(statusCode)
				.time(lessThan(responseTime)).extract().response();

		try {
			String responseBody = response.getBody().asString();
			logger.info("Response Status Code: {}", response.getStatusCode());
			logger.info("Response Body Length: {}", responseBody.length());
			logger.info("Response Body (JSON):\n{}", responseBody);
		} catch (Exception e) {
			logger.error("ERROR capturing response body: ", e);
			logger.error("Exception message: {}", e.getMessage());
		}
		logger.info("########### PATCH REQUEST COMPLETED ###########\n");

		return response;
	}


	/**
	 * DELETE Request
	 */
	public static Response deleteRequest(String baseuri, String basepath, HashMap<String, Object> headers,HashMap<String, ?>  pathParameters,
		 int statusCode, long responseTime) {

		logger.debug("########### DELETE REQUEST STARTED ###########");
		logger.debug("Base URI: {}", baseuri);
		logger.debug("Base Path: {}", basepath);
		// logger.debug("Request Body (JSON):\n{}", serializeToJson(deleteReqBody));

		if (pathParameters != null && !pathParameters.isEmpty()) {
			logger.debug("Path Parameters: {}", pathParameters);
		}
 
		var givenRequest = RestAssured.given().log().all().baseUri(baseuri).contentType(ContentType.JSON);
	
		if (headers != null && !headers.isEmpty()) {
			givenRequest.headers(headers);
		}
 
		if (pathParameters != null && !pathParameters.isEmpty()) {
			givenRequest.pathParams(pathParameters);
		}
		
		Response response = givenRequest.when().delete(basepath).then().log().all().statusCode(statusCode)
				.time(lessThan(responseTime)).extract().response();

		try {
			String responseBody = response.getBody().asString();
			logger.info("Response Status Code: {}", response.getStatusCode());
			logger.info("Response Body Length: {}", responseBody.length());
			logger.info("Response Body (JSON):\n{}", responseBody);
		} catch (Exception e) {
			logger.error("ERROR capturing response body: ", e);
			logger.error("Exception message: {}", e.getMessage());
		}
		logger.info("########### DELETE REQUEST COMPLETED ###########\n");

		return response;
	}
}