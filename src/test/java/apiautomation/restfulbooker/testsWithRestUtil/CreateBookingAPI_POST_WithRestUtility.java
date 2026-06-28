package apiautomation.restfulbooker.testsWithRestUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import apiautomation.restfulbooker.RestUtility.RestUtil;
import apiautomation.restfulbooker.constants.Constants;
import apiautomation.restfulbooker.pojos.BookingDatesPOJO;
import apiautomation.restfulbooker.pojos.CreateBookingPOJO;
import apiautomation.restfulbooker.utils.FakerBookingTestUtility;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CreateBookingAPI_POST_WithRestUtility {
	
	private static final Logger logger = LogManager.getLogger(CreateBookingAPI_POST_WithRestUtility.class);
	
	@BeforeClass
	public static void beforeClass() {
		
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		
	}
	

	
	@Test(priority=1, enabled=false)
	public void createBookingWithHashMap_withRestUtilV1() {
	
		HashMap<String, Object> headers = new HashMap<String, Object>();
		headers.put("Content-Type", "application/json");
		
		HashMap<String, Object> bookingDates = new HashMap<String, Object>(); // child object within bookingData
		bookingDates.put("checkin", "2026-07-01");
		bookingDates.put("checkout", "2026-07-06");
		
		
		HashMap<String, Object> bookingData = new HashMap<String, Object>();
		bookingData.put("firstname", "Test firstname one");
		bookingData.put("lastname", "Test lastname one");
		bookingData.put("totalprice", 1000);
		bookingData.put("depositpaid",false);
		bookingData.put("bookingdates", bookingDates);
		bookingData.put("additionalneeds", "TV");
		
		Response res = RestUtil.postRequest(Constants.BaseURI, Constants.BasePath, headers,bookingData, 200, 6000L);
		System.out.print(res.toString());
		
	}	
	
	@Test(priority=2, enabled=true)
	public void createBookingWithPOJO_withRestUtilV1() {
		
		logger.info("Tests Started");
		
		HashMap<String, Object> headers = new HashMap<String, Object>();
		headers.put("Content-Type", "application/json");
		
		CreateBookingPOJO createBooking = FakerBookingTestUtility.getValidBooking();
		
		Response res = RestUtil.postRequest(Constants.BaseURI, Constants.BasePath, headers,createBooking, 201, 6000L);
		System.out.print(res.toString());
		logger.info("Tests Completed");
	}	
}
