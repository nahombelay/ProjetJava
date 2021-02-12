package ChatSystem.communications;

import java.io.IOException;
import java.net.*;


public class SendUDP {

	private static DatagramSocket socket = null;
	
	/**
	 * Method used to send udp packets on our local network. UDP packets will only be used to transport 
	 * information relative to ActiveUsers
	 * @param Message string that will be send inside the UDP packet
	 * @param address destination address
	 * @param port destination port
	 * @param broadcast boolean that specifies whether or not it is a broadcast UDP packet
	 * @throws IOException
	 */
	public static void send(String Message, InetAddress address, int port, boolean broadcast) throws IOException {
	        socket = new DatagramSocket();
	        socket.setBroadcast(broadcast);

	        byte[]buffer = Message.getBytes();

	        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
	        
	        socket.send(packet);
	        socket.close();
	        
	}
	

	
}
