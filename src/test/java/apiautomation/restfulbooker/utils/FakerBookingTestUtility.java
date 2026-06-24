package apiautomation.restfulbooker.utils;

import java.util.concurrent.TimeUnit;

import com.github.javafaker.Faker;

import apiautomation.restfulbooker.pojos.*;

public class FakerBookingTestUtility {
	
	static Faker faker = new Faker();
	
	static String checkin = faker.date().future(10, TimeUnit.DAYS).toInstant().toString().split("T")[0];
	static String checkout = faker.date().future(20, TimeUnit.DAYS).toInstant().toString().split("T")[0];
	
	
	public static CreateBookingPOJO getValidBooking() {
	
				BookingDatesPOJO bookingdates = new BookingDatesPOJO(checkin,checkout);
				return new CreateBookingPOJO(faker.name().firstName(),faker.name().lastName(),faker.number().numberBetween(1000, 100000),
						true,bookingdates,faker.options().option("TV","Books","Fridge"));
				
	}
	
	public static CreateBookingPOJO partialUpdateValidBooking() {
		
		BookingDatesPOJO bookingdates = new BookingDatesPOJO(checkin,checkout);
		return new CreateBookingPOJO(faker.name().firstName(),faker.name().lastName(),faker.number().numberBetween(1000, 100000),
				true,bookingdates,faker.options().option("TV","Books","Fridge"));
		
}
	
	
	public static CreateBookingPOJO inValidBookingWithOutFirstName() {
		
		BookingDatesPOJO bookingdates = new BookingDatesPOJO(checkin,checkout);
		return new CreateBookingPOJO(null,faker.name().lastName(),faker.number().numberBetween(1000, 100000),
				true,bookingdates,faker.options().option("TV","Books","Fridger"));
		
}
	
	public static CreateBookingPOJO inValidBookingPrice() {
		
		BookingDatesPOJO bookingdates = new BookingDatesPOJO(checkin,checkout);
		return new CreateBookingPOJO(faker.name().firstName(),faker.name().lastName(),-1000,
				true,bookingdates,faker.options().option("TV","Books","Fridger"));
		
}

}
