package src.agent;

import java.io.IOException;
import java.net.Socket;

import src.communications.sendTCP;

public class BindToPeer {
	
	private String ip;
	private int port;
	private static Socket socket;
	
	public BindToPeer(String ip, int port) {
		this.ip = ip;
		this.port = port;
		try {
			this.socket = new Socket(this.ip,this.port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
	
}