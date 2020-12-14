package agent;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import communications.sendTCP;
import database.MessagesDB;
import messages.Timestamp;

public class ConversationOutput extends Thread {
	
	private sendTCP stcp;
	private Scanner scan;
	private Socket socketOutput;
	private MessagesDB messagesDB;
	private final String ipDest;

	/**
	 * Constructor 
	 * Initialize one scanner per conversation
	 * @param socketOutput : used to create a sendTCP object per conversation
	 */
	public ConversationOutput(Socket socketOutput, MessagesDB messagesDB) {
		this.messagesDB = messagesDB;
		this.socketOutput = socketOutput;
		try {
			this.stcp = new sendTCP(socketOutput);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ipDest = socketOutput.getRemoteSocketAddress().toString().substring(1);
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
			messagesDB.addMessage(ipDest, true, msg);
			//stocker le message dans la base de donnée avec timestamp
		}
	
		
	}

}