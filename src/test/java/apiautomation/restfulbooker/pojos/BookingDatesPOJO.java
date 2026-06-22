package apiautomation.restfulbooker.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//default Constructor
//Argument constructor or parameterized constructor
//getters
//setters
// are replace with //Lombok


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingDatesPOJO {
	
	    private String checkin;
	    private String checkout;	
		
		/* below code is replaced with @getter & @Setter from lombok
	    public String getCheckin() {
			return checkin;
		}
		public void setCheckin(String checkin) {
			this.checkin = checkin;
		}
		public String getCheckout() {
			return checkout;
		}
		public void setCheckout(String checkout) {
			this.checkout = checkout;
		}
		*/
	

}
