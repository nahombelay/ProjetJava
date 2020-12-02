package communications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class sendTCP {
	
	private Socket socket;
	private PrintStream out;

	public sendTCP(Socket socket) throws IOException {
		// TODO Auto-generated constructor stub
	    this.out = new PrintStream(socket.getOutputStream());
	}
	
	
	public void sendTextTCP(String message) {
		out.println(message);
	}
	
	/**
	 * One server thread that listens to new incoming conversations permanently 
	 * One client thread for each new conversation
	 * faut quand même établir proprement la connexion
	 */
	
	
	public static void main(String[] args) throws IOException {
		ServerSocket socketServeur = new ServerSocket(12000);
		sendTCP st = new sendTCP(socketServeur.accept());
		
		st.sendTextTCP("hello");
		
		
	}

}
