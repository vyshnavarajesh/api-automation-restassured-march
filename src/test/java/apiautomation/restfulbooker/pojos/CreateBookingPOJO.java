package apiautomation.restfulbooker.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateBookingPOJO {
	
		private String firstname;
		private String lastname;
		private int totalprice;
		private boolean depositpaid;
		private BookingDatesPOJO bookingdates;
		private String additionalneeds;

}
