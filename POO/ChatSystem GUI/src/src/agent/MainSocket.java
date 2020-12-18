package src.agent;

import java.io.IOException;
import java.net.ServerSocket;

public class MainSocket {
	
	final static int port = 10000;
	private ServerSocket mainSocket;
	
	
	/**
	 * Main Socket, handles the server side of the connexion per each user
	 */
	public MainSocket() {
		try {
			this.mainSocket = new ServerSocket(port);
			System.out.println("[MainSocket] Création socket principal");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public ServerSocket getSocketServeur() {
		return mainSocket;
	}

	/**
	 * Close all conversations
	 * @throws IOException
	 */
	public void closeSocket() throws IOException {
		mainSocket.close();
	}

}
