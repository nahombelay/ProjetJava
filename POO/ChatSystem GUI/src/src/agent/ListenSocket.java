package src.agent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import src.database.MessagesDB;

public class ListenSocket extends Thread {
	
	private ServerSocket serverSocket;
	private Socket socketConversation;
	private MessagesDB messagesDB;

    /**
     * 
     * @param socket : take Server socket from mainSocket
     */
	public ListenSocket(ServerSocket socket, MessagesDB messagesDB) {
		this.serverSocket = socket;
		this.messagesDB = messagesDB;
	}
	
	/**
	 * At each new bind creates a thread for sending and receiving messages
	 */
	public void run() {
		System.out.println("[ListenSocket] Attente bind...");
		while (true) {
			try {
				socketConversation = serverSocket.accept();
				System.out.println("[ListenSocket] New accept, socket " + socketConversation.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ConversationInput ci = new ConversationInput(socketConversation, messagesDB);
			ConversationOutput co = new ConversationOutput(socketConversation, messagesDB);
	        ci.start();
	        co.start();
	      }
		
	}

}
