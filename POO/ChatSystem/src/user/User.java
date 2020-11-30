package user;

import java.io.IOException;
import java.net.*;
import java.util.*;

import agent.*;

public class User {
	
	private Login login; 
	private InetAddress ip;
	private String ipString; 

	public User() throws UnknownHostException, IOException {
		
		//lancer thread ListenUsers 
		ListenUsers lu = new ListenUsers();
		lu.start();
		System.out.println("[Users] Thread ListenUsers started");
		
		//get IP address       
        ip = this.getLocalAddress();
		ipString = ip.toString();
		//System.out.println("IP Address : "+ ipString);
		
		//Login initialisation 
		login = new Login("User:" + ipString);
		
		
		BroadcastUDP.broadcast(login.getLogin(), InetAddress.getByName("255.255.255.255"), 20000);
	
		
		/** dans un thread lancer ça
		 * System.out.println("[User]: Establishing connectino i.e opening listening server");
			//Establish connection 
			ThreadServer t = new ThreadServer();
		 */
	}
	
	
	/**
	 * Return the local address of the current user
	 * @return InetAddress
	 */
	private InetAddress getLocalAddress(){
        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while( b.hasMoreElements()){
                for ( InterfaceAddress f : b.nextElement().getInterfaceAddresses())
                    if ( f.getAddress().isSiteLocalAddress())
                        return f.getAddress();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		User u = new User();
	}
}
