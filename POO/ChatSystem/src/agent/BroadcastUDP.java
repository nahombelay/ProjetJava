package agent;

import java.io.IOException;
import java.net.*;
import java.util.*;


public class BroadcastUDP {

	private static DatagramSocket socket = null;
	
	public static void broadcast(String broadcastMessage, InetAddress address, int port) throws IOException {
	        socket = new DatagramSocket();
	        socket.setBroadcast(true);

	        byte[]buffer = broadcastMessage.getBytes();

	        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
	        
	        socket.send(packet);
	        socket.close();
	        
	}
	

	
}
