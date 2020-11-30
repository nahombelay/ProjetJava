package agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class ThreadTCP extends Thread{

	private Socket socket;
	
	public ThreadTCP(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
		      String message = "";

		      System.out.println("Connexion avec le client : " + socket.getInetAddress());

		      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		      PrintStream out = new PrintStream(socket.getOutputStream());
		      message = in.readLine();
		      out.println("Bonjour " + message);

		      //BroadcastUDP.broadcast("Hello", InetAddress.getByName("255.255.255.255"), 20000);
		      socket.close();
		      
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	
	/**
	 * Ecouter sur un port (pour udp) : nc -ul 10000
	 */
	
}
