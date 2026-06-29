package apiautomation.restfulbooker.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;

/**
 * CSV Data Provider for reading test data from CSV files
 */
public class CSVDataProvider {
	
	private static final Logger logger = LogManager.getLogger(CSVDataProvider.class);

	/**
	 * Reads CSV file and returns data as Object[][] for DataProvider
	 * First row is considered as headers
	 */
	public static Object[][] readCSV(String filePath) {
		List<Map<String, String>> dataList = new ArrayList<>();
		String[] headers = null;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			int lineNumber = 0;

			while ((line = reader.readLine()) != null) {
				if (lineNumber == 0) {
					// First line is headers
					headers = line.split(",");
					logger.debug("CSV Headers: {}", (Object[]) headers);
				} else {
				
					String[] values = line.split(",");
					Map<String, String> row = new HashMap<>();

					for (int i = 0; i < headers.length; i++) {
						row.put(headers[i].trim(), values[i].trim());
					}
					dataList.add(row);
					logger.debug("CSV Row {}: {}", lineNumber, row);
				}
				lineNumber++;
			}

			logger.info("Total CSV data rows: {}", dataList.size());

		} catch (IOException e) {
			logger.error("Error reading CSV file: {}", filePath, e);
			throw new RuntimeException("Failed to read CSV file: " + filePath, e);
		}

		Object[][] result = new Object[dataList.size()][1];
		for (int i = 0; i < dataList.size(); i++) {
			result[i][0] = dataList.get(i);
		}

		return result;
	}

	/**
	 * DataProvider method for booking creation tests
	 * Provides data from testData/bookings.csv
	 */
	@DataProvider(name = "bookingData")
	public Object[][] getBookingData() {
		String filePath = "src/test/resources/testData/bookings.csv";
		return readCSV(filePath);
	}

	// To extract specific value from data row
	public static String getValue(Map<String, String> dataRow, String key) {
		return dataRow.getOrDefault(key, "");
	}

	//To convert string to boolean
	 
	public static boolean getBooleanValue(Map<String, String> dataRow, String key) {
		String value = getValue(dataRow, key);
		return Boolean.parseBoolean(value);
	}

	// Helper method to convert string to int
	
	public static int getIntValue(Map<String, String> dataRow, String key) {
		try {
			return Integer.parseInt(getValue(dataRow, key));
		} catch (NumberFormatException e) {
			logger.error("Error converting {} to int", key, e);
			return 0;
		}
	}
}
