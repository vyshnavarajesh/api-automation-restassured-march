package apiautomation.restfulbooker.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import apiautomation.restfulbooker.pojos.BookingDatesPOJO;
import apiautomation.restfulbooker.pojos.CreateBookingPOJO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CreateBookingAPI_POST {
	
	@Test(priority=1, enabled=false)
	public void createBookingWithHashMap() {
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
		
		RestAssured
					.given().log().all().baseUri("https://restful-booker.herokuapp.com")
					.contentType(ContentType.JSON)
					.header("Content-Type","application/json")
					.body(bookingData)
					.when()
					.post("/booking")
					.then().log().body()
					.statusCode(200)
					.time(lessThan(5000L)) // responsetime - lessThan is coming from hamcrest dependency
					.body("booking.firstname",equalTo("Test firstname one")); //equalTo is coming from hamcrest dependency
	}
	
	@Test(priority=2, enabled=false)
	public void createBookingWithPOJO() {
	
		BookingDatesPOJO bookingdates = new BookingDatesPOJO("2026-10-10","2026-11-11");
		CreateBookingPOJO createBooking = new CreateBookingPOJO("Test FirstName","Test LastName",1000,true,bookingdates,"TV");
		
		RestAssured
					.given().log().all().baseUri("https://restful-booker.herokuapp.com")
					.contentType(ContentType.JSON)
					.header("Content-Type","application/json")
					.body(createBooking)
					.when()
					.post("/booking")
					.then().log().body()
					.statusCode(200)
					.time(lessThan(50000L)) // responsetime - lessThan is coming from hamcrest dependency
					.body("booking.firstname",equalTo("Test FirstName")); //equalTo is coming from hamcrest dependency
	}
	
	@Test(priority=3, enabled=true)
	public void createBookingWithJSONFile() throws FileNotFoundException {
		
		File f = new File(System.getProperty("user.dir")+"/src/test/resources/jsonFiles/createBookingReqBody.json");
		
		FileReader fr = new FileReader(f);
		JSONTokener jwt = new JSONTokener(fr);
		JSONObject bookingdata = new JSONObject(jwt);
		
		RestAssured
					.given().log().all().baseUri("https://restful-booker.herokuapp.com")
					.contentType(ContentType.JSON)
					.header("Content-Type","application/json")
					.body(bookingdata.toString())
					.when()
					.post("/booking")
					.then().log().body()
					.statusCode(200)
					.time(lessThan(50000L)) // responsetime - lessThan is coming from hamcrest dependency
					.body("booking.firstname",equalTo("test firstname"))//equalTo is coming from hamcrest dependency
					.body(matchesJsonSchemaInClasspath("schemas/createBookingAPISchema.json"));
	}

}
