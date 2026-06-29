package apiautomation.restfulbooker.constants;

public class Constants {
	
	public static final String BaseURI = "https://restful-booker.herokuapp.com";
	public static final String BasePath = "/booking";
	public static final String AuthorizationEndPoint = "/auth";
	public static final String CreateBookingBasePath = "/booking"; // this is an example
	public static final String GetBookingBasePath = "/booking/{bookingId}";
	public static final String updateBookingBasePath = "/booking/{bookingId}";
	public static final String partialUpdateBookingBasePath = "/booking/{bookingId}";
	public static final String DeleteBookingBasePath = "/booking/{bookingId}";
	
	public static final String createBookingAPISchema = "schemas/createBookingAPISchema.json";
	public static final String getBookingAPISchema = "schemas/getBookingAPISchema.json";
	public static final String updateBookingAPISchema = "schemas/updateBookingAPISchema.json";
	public static final String partialUpdateBookingAPISchema = "schemas/partialUpdateBookingAPISchema.json";
	
	public static final String RestDevAPI_BaseURI = "https://api.restful-api.dev";
	
	
	

}
