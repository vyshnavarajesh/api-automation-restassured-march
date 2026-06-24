package apiautomation.restfulbooker.tests;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

import org.testng.annotations.Test;

import apiautomation.restfulbooker.constants.Constants;
import apiautomation.restfulbooker.pojos.BookingDatesPOJO;
import apiautomation.restfulbooker.pojos.CreateBookingPOJO;
import apiautomation.restfulbooker.utils.FakerBookingTestUtility;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class CreateBookingAPI_Faker_POST {
	
	@Test(priority=1, enabled=true)
	public void createBookingWithPOJOFajerData() {

		CreateBookingPOJO createBooking = FakerBookingTestUtility.getValidBooking();
		
		RestAssured
					.given().log().all().baseUri(Constants.BaseURI)
					.contentType(ContentType.JSON)
					//.header("Content-Type","application/json")
					.body(createBooking)
					.when()
					.post(Constants.BasePath)
					.then().log().body()
					.statusCode(200)
					.time(lessThan(50000L)); // responsetime - lessThan is coming from hamcrest dependency

	}

}
