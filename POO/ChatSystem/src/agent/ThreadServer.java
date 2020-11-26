package agent;

import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServer {
	
	final static int port = 10000;
	
	/**
	 * User is connected or not -> server is active or not 
	 */
	
	public ThreadServer() {
		try {
		      ServerSocket socketServeur = new ServerSocket(port);
		      System.out.println("Lancement du serveur");
		      while (true) {
		        Socket socketClient = socketServeur.accept();
		        ThreadTCP t = new ThreadTCP(socketClient);
		        t.start();
		      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	
	public static void main(String[] args) {
		ThreadServer th = new ThreadServer();
	}
	
}
