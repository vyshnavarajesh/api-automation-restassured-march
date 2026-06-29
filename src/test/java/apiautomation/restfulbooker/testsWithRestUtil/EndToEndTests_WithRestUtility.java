package apiautomation.restfulbooker.testsWithRestUtil;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import apiautomation.restfulbooker.RestUtility.RestUtil;
import apiautomation.restfulbooker.constants.Constants;
import apiautomation.restfulbooker.pojos.CreateBookingPOJO;

import apiautomation.restfulbooker.utils.FakerBookingTestUtility;
import apiautomation.restfulbooker.utils.TokenGenUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class EndToEndTests_WithRestUtility {
	
	private static final Logger logger = LogManager.getLogger(EndToEndTests_WithRestUtility.class);
	
	/**
	 * Creates path parameters HashMap with booking ID
	 */
	private HashMap<String, Object> createPathParams(int bookingId) {
		HashMap<String, Object> pathParams = new HashMap<>();
		pathParams.put("bookingId", bookingId);  
		return pathParams;
	}

	/**
	 * Creates headers HashMap with authentication token
	 */
	private HashMap<String, Object> createAuthHeaders(String token) {
		HashMap<String, Object> headers = new HashMap<>();
		headers.put("Cookie", "token=" + token);
		return headers;
	}

	/**
	 * Creates headers HashMap for API request 
	 */
	private HashMap<String, Object> createPostHeaders() {
		HashMap<String, Object> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return headers;
	}

	/**
	 * Validates successful booking creation
	 */

	@BeforeClass
	public void beforeClass(ITestContext context)
	{
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		/* Token Generation */
		String tokenValue = TokenGenUtil.tokenGenerate();
		
		logger.info("Tests Started");
		
		HashMap<String, Object> headers = new HashMap<String, Object>();
		headers.put("Content-Type", "application/json");
		
		CreateBookingPOJO createBooking = FakerBookingTestUtility.getValidBooking();
		
		context.setAttribute("firstName", createBooking.getFirstname());
		
		Response res = RestUtil.postRequest(Constants.BaseURI, Constants.BasePath, headers,createBooking, 200, 6000L);
		
		res.then().assertThat()
		//.body("bookingid", notNullValue())
        .body("bookingid", greaterThan(0));
		
		System.out.print(res.toString());
		logger.info("Tests Completed");
		
		int bookingId = res.jsonPath().getInt("bookingid");
		
		/* setting the context value */
		context.setAttribute("bookingId",bookingId);
		context.setAttribute("tokenValue", tokenValue);
	}
	
	@Test (priority=1)
	//reusing get info for update call
	public void updateBookingWithGet(ITestContext context)
	{
		/* setting the context value */
		int bookingID = (int) context.getAttribute("bookingId");
		String token = (String) context.getAttribute("tokenValue");
		
		/*	
		 * Replace below code with => createAuthHeaders(token),createPathParams(bookingID)
		 * 
		 * HashMap<String, Object> pathParams = new HashMap<>();
			pathParams.put("bookingId", bookingID);
			
			HashMap<String, Object> tokenHeaders = new HashMap<String, Object>();
			tokenHeaders.put("Cookie", "token=" + token);
			*/
		Response getResponseBody = RestUtil.getRequest(Constants.BaseURI, Constants.GetBookingBasePath, null,createPathParams(bookingID), 200, 6000L);
		System.out.print(getResponseBody.toString());
		
		// Asserting that firstName setup in Post Call is matching with Get Call
		String actual_firstname= getResponseBody.jsonPath().getString("firstname");
		String expected_firstname = (String) context.getAttribute("firstName"); 
		
		Assert.assertEquals(actual_firstname, expected_firstname,"Firstname is not matching get x post");
		
		Map<String,Object> updateData = getResponseBody.jsonPath().getMap("$");
		updateData.put("firstname", "Test firstname one update");
		updateData.put("lastname", "Test lastname one update");
		
		Response putResponseBody = RestUtil.putRequest(Constants.BaseURI, Constants.GetBookingBasePath,
				createAuthHeaders(token),createPathParams(bookingID), updateData,200, 6000L);
		System.out.print("updated firstName ===> "+putResponseBody.jsonPath().getString("firstname"));
		
		putResponseBody.then().assertThat()
	        .statusCode(200)
	        .body("firstname", equalTo("Test firstname one update"));
		
	}
	
	@Test(priority=2,enabled=true)
	public void partialupdateBooking(ITestContext context)
	{

		/* setting the context value */
		int bookingID = (int) context.getAttribute("bookingId");
		String token = (String) context.getAttribute("tokenValue");
		
		HashMap<String, Object> updateBookingData = new HashMap<String, Object>();
		updateBookingData.put("firstname", "Test firstname update");
		updateBookingData.put("lastname", "Test lastname update");
		updateBookingData.put("totalprice", 5000);
	
	/*	
	 * Replace below code with => createAuthHeaders(token),createPathParams(bookingID)
	 * 
	 * HashMap<String, Object> pathParams = new HashMap<>();
		pathParams.put("bookingId", bookingID);
		
		HashMap<String, Object> tokenHeaders = new HashMap<String, Object>();
		tokenHeaders.put("Cookie", "token=" + token);
		*/
		
		Response patchResponseBody = RestUtil.patchRequest(Constants.BaseURI, Constants.GetBookingBasePath, createAuthHeaders(token),createPathParams(bookingID)
				,updateBookingData
				,200, 6000L);
		System.out.print(patchResponseBody.toString());
		
		patchResponseBody.then().assertThat()
        .statusCode(200)
        .body("firstname", equalTo("Test firstname update"));
	}
	
	
	@Test(enabled=false)
	public void deleteBooking(ITestContext context)
	{
		
		/* setting the context value */
		int bookingID = (int) context.getAttribute("bookingId");
		String token = (String) context.getAttribute("tokenValue");
		
		HashMap<String, Object> pathParams = new HashMap<>();
		pathParams.put("bookingId", bookingID);
		
		HashMap<String, Object> tokenHeaders = new HashMap<String, Object>();
		tokenHeaders.put("Cookie", "token=" + token);
		
		Response deleteResponse = RestUtil.deleteRequest(Constants.BaseURI, Constants.GetBookingBasePath, tokenHeaders,pathParams,200, 6000L);
	
	}	
	
	@Test(priority=3)
	public void deleteBooking2(ITestContext context)
	{
		
		/* setting the context value */
		int bookingID = (int) context.getAttribute("bookingId");
		String token = (String) context.getAttribute("tokenValue");
		
		Response deleteResponse = RestUtil.deleteRequest(Constants.BaseURI, Constants.GetBookingBasePath, createAuthHeaders(token),createPathParams(bookingID)
				,201, 6000L);
	
	}	

}

