package messages;

import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class

public class TextMessage {
	private String message;
	private LocalDateTime timestamp;
	
	public TextMessage(String message) {
		this.message = message;
		this.timestamp = LocalDateTime.now();
		
	}

	public String formatDateTime() {
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		return timestamp.format(myFormatObj);
	}
	
	public String getMessage() {
		return message;
	}
  
	
	@Override
	public String toString(){
		return formatDateTime() + " - " + message;
	}
	
	
	
}

