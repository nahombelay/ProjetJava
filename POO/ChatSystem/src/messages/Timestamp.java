package messages;

import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class

public class Timestamp {
	private static LocalDateTime timestamp;
	private static DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	public static String formatDateTime() {
		timestamp = LocalDateTime.now();
		return timestamp.format(myFormatObj);
	}	
	
}

