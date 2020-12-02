package agent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServer {
	
	final static int port = 10000;
	private ServerSocket socketServeur;
	private Socket socketClient;
	
	
	/**
	 * User is connected or not -> server is active or not 
	 */
	
	public ThreadServer() {
		try {
		      socketServeur = new ServerSocket(port);
		      System.out.println("Lancement du serveur");
		      while (true) {
		        socketClient = socketServeur.accept();
		        ThreadTCP t = new ThreadTCP(socketClient);
		        t.start();
		      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	
	public void closeSocket() throws IOException {
		socketServeur.close();
		socketClient.close();
		
	}
	
	public static void main(String[] args) {
		ThreadServer th = new ThreadServer();
	}
	
}
