package agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
	private static Socket clientSocket;
	private static BufferedReader in;
	
	public Client (String ip, int port) throws IOException {
		clientSocket = new Socket(ip, port);
	}
	
	public static void main(String[] args) throws IOException {
		
		Client C = new Client("127.0.0.1", 6666);
		
		System.out.println("[Client] Connection acceptée");
		
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String resp = in.readLine();
		
		System.out.println("[Client] Reception de la date : " + resp);
       
		
        System.out.println("[Client] Date Reçu");
		in.close();
		clientSocket.close();
	}

}
