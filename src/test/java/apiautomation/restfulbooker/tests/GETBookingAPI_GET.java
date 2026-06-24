package apiautomation.restfulbooker.tests;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import apiautomation.restfulbooker.constants.Constants;
import apiautomation.restfulbooker.pojos.BookingDatesPOJO;
import apiautomation.restfulbooker.pojos.CreateBookingPOJO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GETBookingAPI_GET {
	
	//@BeforeClass  - use it if you need one booking ID for multiple test
	
	int bookingId;
	@BeforeMethod // use it when booking ID needs to be created before every test method
	public void beforeMethod()
	{
		BookingDatesPOJO bookingdates = new BookingDatesPOJO("2026-10-10","2026-11-11");
		CreateBookingPOJO createBooking = new CreateBookingPOJO("Test FirstName","Test LastName",1000,true,bookingdates,"TV");
		
		Response res = RestAssured
					.given().log().all().baseUri(Constants.BaseURI)
					.contentType(ContentType.JSON)
					//.header("Content-Type","application/json")
					.body(createBooking)
					.when()
					.post("/booking")
					.then().log().body()
					.statusCode(200)
					.time(lessThan(50000L)) // responsetime - lessThan is coming from hamcrest dependency
					.extract().response();
		
		bookingId = res.jsonPath().getInt("bookingid");
	}
	
	@Test
	public void getBookingInfo()
	{
		RestAssured.
				given().baseUri(Constants.BaseURI).log().all()
							.contentType(ContentType.JSON) // pre condition
							.pathParam("bookingID", bookingId)
							.when()
							.get(Constants.BasePath+"/{bookingID}") // actual test
							.then().assertThat().statusCode(200) // assertions
							.statusLine("HTTP/1.1 200 OK")
							.header("Content-Type","application/json; charset=utf-8")
							.time(lessThan(5000L))
							.log().all()
							.body("firstname", equalTo("Test FirstName"));

	}

}
