package src.agent;

import java.net.Socket;

public class ListenHttp extends Thread {
	
	private String ServerURL;
	private int ServerPort;
	private Socket socket;
	
	public ListenHttp() {
		this.ServerURL = "http://localhost:8080/ChatSystem_Server/toto";
		this.ServerPort = 8080;
		this.socket = new Socket();
		
		
	}
	
	public void run() {
		
	}
}
