package agent;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import communications.sendTCP;
import messages.Timestamp;

public class ConversationOutput extends Thread {
	
	private sendTCP stcp;
	private Scanner scan;

	/**
	 * Constructor 
	 * Initialize one scanner per conversation
	 * @param socketOutput : used to create a sendTCP object per conversation
	 */
	public ConversationOutput(Socket socketOutput) {
		try {
			this.stcp = new sendTCP(socketOutput);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.scan = new Scanner(System.in);
	}
	/**
	 * Waits for an input from the console and sends it throught sendTextTCP
	 * 
	 * A AMELIORER
	 */
	public void run()  {
		
		String msg = null;
		System.out.println("[ConversationOutput] You can now enter a message...");
		while(true) {
			msg = scan.nextLine();
			stcp.sendTextTCP("[ConversationOutput] " + msg);
			
			String timestamp = Timestamp.formatDateTime();
			//stocker le message dans la base de donnée avec timestamp
		}
	
		
	}

}