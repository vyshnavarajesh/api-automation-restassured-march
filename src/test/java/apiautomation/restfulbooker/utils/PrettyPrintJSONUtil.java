package apiautomation.restfulbooker.utils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrettyPrintJSONUtil {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static String prettyPrintJson(String jsonString) {
	    if (jsonString == null || jsonString.trim().isEmpty()) {
	        return jsonString;
	    }
	    try {
	        Object json = objectMapper.readValue(jsonString, Object.class);
	        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
	    } catch (Exception e) {
	        // Not a valid JSON, return as is
	        return jsonString;
	    }
	}
	
}
