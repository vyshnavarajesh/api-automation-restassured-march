package apiautomation.restfulbooker.tests;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

import java.util.HashMap;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import apiautomation.restfulbooker.constants.Constants;
import apiautomation.restfulbooker.pojos.BookingDatesPOJO;
import apiautomation.restfulbooker.pojos.CreateBookingPOJO;
import apiautomation.restfulbooker.utils.TokenGenUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class UpdateBookingAPI_PUT {
	
	@BeforeMethod
	public void beforeMethod(ITestContext context)
	{
		/* Token Generation */
		String tokenValue = TokenGenUtil.tokenGenerate();
		
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
		
		Response res = RestAssured
					.given().log().all().baseUri(Constants.BaseURI)
					.contentType(ContentType.JSON)
					.header("Content-Type","application/json")
					.body(bookingData)
					.when()
					.post("/booking")
					.then().log().body()
					.statusCode(200)
					.time(lessThan(5000L)) // responsetime - lessThan is coming from hamcrest dependency
					// responsetime - lessThan is coming from hamcrest dependency
					.extract().response();
		
		int bookingId = res.jsonPath().getInt("bookingid");
		
		/* setting the context value */
		context.setAttribute("bookingId",bookingId);
		context.setAttribute("tokenValue", tokenValue);
	}
	
	@Test(enabled=false)
	public void updateBooking(ITestContext context)
	{
		
		/* setting the context value */
		int bookingID = (int) context.getAttribute("bookingId");
		String token = (String) context.getAttribute("tokenValue");
		
		HashMap<String, Object> bookingDates = new HashMap<String, Object>(); // child object within bookingData
		bookingDates.put("checkin", "2026-07-01");
		bookingDates.put("checkout", "2026-07-06");
		
		HashMap<String, Object> updateBookingData = new HashMap<String, Object>();
		updateBookingData.put("firstname", "Test firstname update");
		updateBookingData.put("lastname", "Test lastname update");
		updateBookingData.put("totalprice", 5000);
		updateBookingData.put("depositpaid",true);
		updateBookingData.put("bookingdates", bookingDates);
		updateBookingData.put("additionalneeds", "TV");
		
		
	RestAssured
					.given().log().all().baseUri(Constants.BaseURI)
					.contentType(ContentType.JSON)
					.header("Cookie","token="+token)
					.pathParam("bookingID", bookingID)
					.body(updateBookingData)
					.when()
					.put("/booking/{bookingID}")
					.then().log().body()
					.statusCode(200)
					.time(lessThan(50000L)) // responsetime - lessThan is coming from hamcrest dependency
					.body("firstname",equalTo("Test firstname update"));
		
	}
	
	@Test
	//reusing get info for update call
	public void updateBookingWithGet(ITestContext context)
	{
		
		/* setting the context value */
		int bookingID = (int) context.getAttribute("bookingId");
		String token = (String) context.getAttribute("tokenValue");
		
		
		Response getresponse = RestAssured.
		given().baseUri(Constants.BaseURI)
					.contentType(ContentType.JSON) // pre condition
					.pathParam("bookingID", bookingID)
					.when()
					.get("/{bookingID}") // actual test
					.then().assertThat().statusCode(200) // assertions
					.statusLine("HTTP/1.1 200 OK")
					.header("Content-Type","application/json; charset=utf-8")
					.time(lessThan(5000L))
					.log().all()
					.body("firstname", equalTo("Test firstname one")).extract().response();
		
		Map<String,Object> updateData = getresponse.jsonPath().getMap("$");
		
		updateData.put("firstname", "Test firstname one update");
		updateData.put("lastname", "Test lastname one update");
		
	RestAssured
					.given().log().all().baseUri(Constants.BaseURI)
					.contentType(ContentType.JSON)
					.header("Cookie","token="+token)
					.pathParam("bookingID", bookingID)
					.body(updateData)
					.when()
					.put("/booking/{bookingID}")
					.then().log().body()
					.statusCode(200)
					.time(lessThan(50000L)) // responsetime - lessThan is coming from hamcrest dependency
					.body("firstname",equalTo("Test firstname one update"));
		
	}

}
