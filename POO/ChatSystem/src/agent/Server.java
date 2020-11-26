package agent;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Date;


public class Server implements Runnable {
	
	private static ServerSocket sock;
	private static Socket link;
	private static PrintWriter out;
	
	public Server (int port) throws IOException {

		sock = new ServerSocket(port);
		try {
			link = sock.accept();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("[Server] Connection acceptée");

	}
	
	
	public void run() {
		
		System.out.println("[Server] Envoie de la date");
		
		Date date = new Date();

		try {
			out = new PrintWriter(link.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println(date.toString());
		
		System.out.println("[Server] Date Envoyée");
		
		out.close();
		try {
			link.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public static void main(String[] args) throws IOException {

			Server S = new Server(5020);
			
			S.run();
	
			sock.close();
	        
			
	
		}
        
		


}
