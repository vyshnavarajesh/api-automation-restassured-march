package apiautomation.restfulbooker.testsWithRestUtil;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;

import apiautomation.restfulbooker.RestUtility.RestUtil;
import apiautomation.restfulbooker.constants.Constants;

import apiautomation.restfulbooker.pojos.BookingDatesPOJO;
import apiautomation.restfulbooker.pojos.CreateBookingPOJO;
import apiautomation.restfulbooker.utils.CSVDataProvider;
import apiautomation.restfulbooker.utils.TokenGenUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class BookingAPITest_CSVDataProvider {
	
	private static final Logger logger = LogManager.getLogger(BookingAPITest_CSVDataProvider.class);
	private CSVDataProvider csvDataProvider;

	@BeforeClass
	public void beforeClass() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		csvDataProvider = new CSVDataProvider();
		
	}

	@BeforeMethod
	public void beforeMethod(ITestResult result) {
		logger.info("========== TEST: {} STARTED ==========", result.getMethod().getMethodName());
	}

	// ==================== HELPER METHODS ====================

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
	 * Creates headers HashMap for POST request (Content-Type only)
	 */
	private HashMap<String, Object> createPostHeaders() {
		HashMap<String, Object> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return headers;
	}

	// Converts CSV data row to CreateBookingPOJO
	
	private CreateBookingPOJO convertDataToPOJO(Map<String, String> dataRow) {
		String firstName = CSVDataProvider.getValue(dataRow, "firstName");
		String lastName = CSVDataProvider.getValue(dataRow, "lastName");
		int totalPrice = CSVDataProvider.getIntValue(dataRow, "totalPrice");
		boolean depositPaid = CSVDataProvider.getBooleanValue(dataRow, "depositPaid");
		String checkIn = CSVDataProvider.getValue(dataRow, "checkIn");
		String checkOut = CSVDataProvider.getValue(dataRow, "checkOut");
		String additionalNeeds = CSVDataProvider.getValue(dataRow, "additionalNeeds");

		BookingDatesPOJO bookingDates = new BookingDatesPOJO(checkIn, checkOut);
		CreateBookingPOJO booking = new CreateBookingPOJO(
			firstName, 
			lastName, 
			totalPrice, 
			depositPaid, 
			bookingDates, 
			additionalNeeds
		);

		logger.debug("POJO created from CSV: firstname={}, lastname={}, price={}", 
			firstName, lastName, totalPrice);

		return booking;
	}

	/**
	 * Validates successful booking creation
	 */
	private int validateBookingCreated(Response response) {
		response.then().assertThat()
			.statusCode(200)
			.body("bookingid", notNullValue())
			.body("bookingid", greaterThan(0));
		
		int bookingId = response.jsonPath().getInt("bookingid");
		logger.info("Booking created successfully with ID: {}", bookingId);
		
		return bookingId;
	}

	/**
	 * Validates booking details match expected values
	 */
	private void validateBookingDetails(Response response, String expectedFirstName, 
		String expectedLastName, int expectedPrice) {
		response.then().assertThat()
			.body("booking.firstname", equalTo(expectedFirstName))
			.body("booking.lastname", equalTo(expectedLastName))
			.body("booking.totalprice", equalTo(expectedPrice))
			.body("bookingid", notNullValue());

		logger.info("Booking details validated: {} {}", expectedFirstName, expectedLastName);
	}

	
	/**
	 * Test: Create multiple bookings using CSV data
	 * DataProvider will iterate through each row in bookings.csv
	 */
	@Test(dataProvider = "bookingData", dataProviderClass = CSVDataProvider.class)
	public void testCreateBookingWithCSVData(Map<String, String> testData) {
		logger.info("Test iteration with data: firstName={}, lastName={}", 
			testData.get("firstName"), testData.get("lastName"));

		/* Convert CSV data to POJO */
		CreateBookingPOJO booking = convertDataToPOJO(testData);

		/* Create booking via REST API */
		Response response = RestUtil.postRequest(
			Constants.BaseURI,
			Constants.BasePath,
			createPostHeaders(),
			booking,
			200,
			6000L
		);

		//Validate response 
		int bookingId = validateBookingCreated(response);
		
		String firstName = CSVDataProvider.getValue(testData, "firstName");
		String lastName = CSVDataProvider.getValue(testData, "lastName");
		int totalPrice = CSVDataProvider.getIntValue(testData, "totalPrice");

		validateBookingDetails(response, firstName, lastName, totalPrice);

		logger.info("========== TEST PASSED ==========\n");
	}

	/**
	 * Test: Create booking, retrieve it, and verify data matches CSV
	 */
	@Test(dataProvider = "bookingData", dataProviderClass = CSVDataProvider.class,enabled=false)
	public void testCreateAndRetrieveBooking(Map<String, String> testData) {
		logger.info("Testing Create and Retrieve with data: {}", testData.get("firstName"));

		CreateBookingPOJO booking = convertDataToPOJO(testData);

		//Create Booking
		Response createResponse = RestUtil.postRequest(
			Constants.BaseURI,
			Constants.BasePath,
			createPostHeaders(),
			booking,
			200,
			6000L
		);

		int bookingId = validateBookingCreated(createResponse);

		String expectedFirstName = CSVDataProvider.getValue(testData, "firstName");
		String expectedLastName = CSVDataProvider.getValue(testData, "lastName");
		int expectedPrice = CSVDataProvider.getIntValue(testData, "totalPrice");

		// Retrieve  Booking
		logger.info("Retrieving booking with ID: {}", bookingId);
		Response getResponse = RestUtil.getRequest(
			Constants.BaseURI,
			Constants.GetBookingBasePath,
			null,
			createPathParams(bookingId),
			200,
			6000L
		);

		/* Validate retrieved data matches created data */
		String retrievedFirstName = getResponse.jsonPath().getString("firstname");
		String retrievedLastName = getResponse.jsonPath().getString("lastname");
		int retrievedPrice = getResponse.jsonPath().getInt("totalprice");

		logger.info("Validating retrieved data matches CSV data");
		Assert.assertEquals(retrievedFirstName, expectedFirstName, 
			"Retrieved firstname doesn't match CSV data");
		Assert.assertEquals(retrievedLastName, expectedLastName, 
			"Retrieved lastname doesn't match CSV data");
		Assert.assertEquals(retrievedPrice, expectedPrice, 
			"Retrieved price doesn't match CSV data");

		logger.info("========== TEST PASSED ==========\n");
	}

	/**
	 * Test: Create booking, update it, and verify changes
	 */
	@Test(dataProvider = "bookingData", dataProviderClass = CSVDataProvider.class)
	public void testCreateUpdateAndVerify(Map<String, String> testData) {
		logger.info("Testing Create, Update, and Verify with data: {}", testData.get("firstName"));

		CreateBookingPOJO booking = convertDataToPOJO(testData);

		String token = TokenGenUtil.tokenGenerate();
		if (token == null || token.isEmpty()) {
			throw new RuntimeException("Failed to generate token");
		}
		logger.info("Token generated for update operation");

		//Create Booking
		Response createResponse = RestUtil.postRequest(
			Constants.BaseURI,
			Constants.BasePath,
			createPostHeaders(),
			booking,
			200,
			6000L
		);

		int bookingId = validateBookingCreated(createResponse);

		Map<String, Object> updateData = createResponse.jsonPath().getMap("booking");
		String updatedFirstName = testData.get("firstName") + "_Updated";
		updateData.put("firstname", updatedFirstName);
		
		logger.info("Updating booking with new firstname: {}", updatedFirstName);

		//update Booking
		Response updateResponse = RestUtil.putRequest(
			Constants.BaseURI,
			Constants.GetBookingBasePath,
			createAuthHeaders(token),
			createPathParams(bookingId),
			updateData,
			200,
			6000L
		);

		// Retrieve & verify booking 
		Response getResponse = RestUtil.getRequest(
			Constants.BaseURI,
			Constants.GetBookingBasePath,
			null,
			createPathParams(bookingId),
			200,
			6000L
		);

		String finalFirstName = getResponse.jsonPath().getString("firstname");
		Assert.assertEquals(finalFirstName, updatedFirstName, 
			"Updated firstname doesn't persist");

		logger.info("========== TEST PASSED ==========\n");
	}

	/**
	 * Test: Create and delete booking for each CSV data row
	 */
	@Test(dataProvider = "bookingData", dataProviderClass = CSVDataProvider.class,enabled=false)
	public void testCreateAndDeleteBooking(Map<String, String> testData) {
		logger.info("Testing Create and Delete with data: {}", testData.get("firstName"));

		CreateBookingPOJO booking = convertDataToPOJO(testData);

		String token = TokenGenUtil.tokenGenerate();
		if (token == null || token.isEmpty()) {
			throw new RuntimeException("Failed to generate token");
		}

		// Create Booking
		Response createResponse = RestUtil.postRequest(
			Constants.BaseURI,
			Constants.BasePath,
			createPostHeaders(),
			booking,
			200,
			6000L
		);

		int bookingId = validateBookingCreated(createResponse);

		// Delete Booking
		logger.info("Deleting booking with ID: {}", bookingId);
		Response deleteResponse = RestUtil.deleteRequest(
			Constants.BaseURI,
			Constants.GetBookingBasePath,
			createAuthHeaders(token),
			createPathParams(bookingId),
			201,
			6000L
		);

		//Verify Booking is deleted - 404 status code
		logger.info("Verifying booking is deleted");
		Response getResponse = RestUtil.getRequest(
			Constants.BaseURI,
			Constants.GetBookingBasePath,
			null,
			createPathParams(bookingId),
			404,  // Expecting 404 after delete
			6000L
		);

		logger.info("Booking successfully deleted and verified");
		logger.info("========== TEST PASSED ==========\n");
	}
}
