package agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import database.MessagesDB;
import messages.Timestamp;

public class ConversationInput extends Thread {
	
	private Socket socketInput;
	private static BufferedReader inputBuffer;
	private MessagesDB messagesDB;
	private final String ipDest; 
    /**
     * 
     * @param socketInput : socket to which we'll be permanently waiting for an input
     */
	public ConversationInput(Socket socketInput, MessagesDB messagesDB) {
		this.messagesDB = messagesDB;
		this.socketInput = socketInput;
		ipDest = socketInput.getRemoteSocketAddress().toString().substring(1);
	}
	/**
	 * At each input read, it prints it. 
	 * If null is received we close the conversation (aka the socket)
	 * A AMELIORER
	 */
	public void run()  {
		
		while(true) {
			try {
				inputBuffer = new BufferedReader(new InputStreamReader(socketInput.getInputStream()));
				String resp = inputBuffer.readLine();
				if (resp == null) {
					System.out.println("[ConversationInput] " + socketInput.toString() + " : End of Conversation.");
					socketInput.close();
					//TODO: cancel thread
					break;
				} 
				String timestamp = Timestamp.formatDateTime();
				System.out.println("[ConversationInput] " + socketInput.toString() + " --- " + timestamp + " : " + resp);
				messagesDB.addMessage(ipDest, false, resp);
				//stocker le message dans la base de donnée
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	public Socket getSocketInput() {
		return socketInput;
	}

}
