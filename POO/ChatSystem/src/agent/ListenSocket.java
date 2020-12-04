package agent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenSocket extends Thread {
	
	private ServerSocket serverSocket;
	private Socket socketConversation;

    /**
     * 
     * @param socket : take Server socket from mainSocket
     */
	public ListenSocket(ServerSocket socket) {
		this.serverSocket = socket;
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
			ConversationInput ci = new ConversationInput(socketConversation);
			ConversationOutput co = new ConversationOutput(socketConversation);
	        ci.start();
	        co.start();
	      }
		
	}

}
