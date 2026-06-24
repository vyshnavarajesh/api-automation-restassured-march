package apiautomation.restfulbooker.tests;

//import static io.restassured.RestAssured.*;

import org.testng.annotations.Test;

import apiautomation.restfulbooker.constants.Constants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.lessThan;

public class GetAllBookingAPI_GET {
	
	@Test
	public static void getAllBookings() {

		Response res =RestAssured.
						given().baseUri(Constants.BaseURI)
									.contentType(ContentType.JSON) // pre condition
									.when()
									.get(Constants.BasePath) // actual test
									.then().assertThat().statusCode(200) // assertions
									.statusLine("HTTP/1.1 200 OK")
									.header("Content-Type","application/json; charset=utf-8")
									.time(lessThan(5000L))
									//.log().all()
									.extract().response();
		
		System.out.print(res.asString());
								
		
		/// 
		/// 
									
	}
	
	
	/* RestAssured.given().
	 // baseUri 
	// contentType
	//header
	// path param
	//query param
	// request Body
	when().
	//you can send HTTP methods get // post // PUT // patch // delete
	
	then()
	// capturing status code
	// asserting data
	//valdidatin schema etc.,
	*/

}
