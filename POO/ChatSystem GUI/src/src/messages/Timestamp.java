package src.messages;

import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class

public class Timestamp {
	private static LocalDateTime timestamp;
	private static DateTimeFormatter myFormatObj;

	public static String formatDateTime() {
		myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		timestamp = LocalDateTime.now();
		return timestamp.format(myFormatObj);
	}	
	
	public static String formatDateTimeFull() {
		myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		timestamp = LocalDateTime.now();
		return timestamp.format(myFormatObj);
	}	
	
}

