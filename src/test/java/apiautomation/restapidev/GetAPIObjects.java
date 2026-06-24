package apiautomation.restapidev;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

import org.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.Test;

import apiautomation.restfulbooker.constants.Constants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GetAPIObjects {
	
	@Test
	public void testGetObjects()
	{
		Response res = RestAssured.
				given().baseUri(Constants.RestDevAPI_BaseURI).log().all()
							.contentType(ContentType.JSON) // pre condition
							.when()
							.get("/objects") // actual test
							.then().assertThat().statusCode(200) // assertions
							.statusLine("HTTP/1.1 200 OK")
							.header("Content-Type","application/json;charset=UTF-8")
							.time(lessThan(5000L))
							.log().all().extract().response();
		
		String responseBody = res.asString();
		
		JSONArray jsArray = new JSONArray(responseBody);
		
		/* To get all the device Name 
		for(int i=0; i<jsArray.length();i++) {
			String deviceName =	jsArray.getJSONObject(i).getString("name");
			System.out.println("=======Device List=========");
			System.out.println(deviceName);
		}
		*/
		
		// Validating if specific device name is available
		boolean deviceStatus = false;
		
		for(int i=0; i<jsArray.length();i++) {
			String deviceName =	jsArray.getJSONObject(i).getString("name");
			System.out.println("=======Device List=========");
			System.out.println(deviceName);
			if(deviceName.equals("Beats Studio3 Wireless"))
			{
				deviceStatus=true;
				break;
			}
		}
		
		Assert.assertEquals(deviceStatus, true);
		
	}

}
