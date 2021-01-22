package src.agent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import src.communications.sendTCP;
import src.database.MessagesDB;
//We don't use this class anymore but it left as an example
public class ConversationOutput extends Thread {
	
	private sendTCP stcp;
	private Scanner scan;
	private Socket socketOutput;
	private MessagesDB messagesDB;
	private final String ipDest;
	public static boolean stop = false;

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
		ipDest = ((InetSocketAddress) socketOutput.getRemoteSocketAddress()).toString().split("/")[1].split(":")[0];
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
		while(!stop) {
			msg = scan.nextLine();
			stcp.sendTextTCP(msg);
	
			messagesDB.addMessage(ipDest, true, msg);
			//stocker le message dans la base de donnée avec timestamp
		}
		System.out.println("[ConversationOutput] End of Thread");
	
		
	}
	public Socket getSocketOutput() {
		return socketOutput;
	}

}