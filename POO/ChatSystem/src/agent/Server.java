package agent;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Date;
import display.DisplayMessage;

public class Server implements Runnable {
	
	private static ServerSocket sock;
	private static Socket link;
	private static PrintWriter out;
	private DisplayMessage DM;  
	
	public Server (int port) throws IOException {

		sock = new ServerSocket(port);
		try {
			link = sock.accept();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		DM = new DisplayMessage();
		
		System.out.println("[Server] Connection acceptée");

	}
	
	
	public void run() {
		
		System.out.println("[Server] Envoie message");
		

		try {
			out = new PrintWriter(link.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println(DM.readInput());
		
		System.out.println("[Server] Message Envoyée");
		
		out.close();
		try {
			link.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public static void main(String[] args) throws IOException {

			Server S = new Server(2222);
			
			S.run();
	
			sock.close();
	        
			
	
		}
        
		


}
