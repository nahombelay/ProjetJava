package user;

import java.io.IOException;
import java.net.*;
import java.util.*;

import agent.*;
import communications.SendUDP;
import database.ActiveUsers;

public class User {
	
	private Login login; 
	private ActiveUsers activeUsers;
	private InetAddress ip;
	private String ipString; 

	public User() throws UnknownHostException, IOException {
		
		//get IP address       
        ip = this.getLocalAddress();
		ipString = ip.toString().substring(1);
		//System.out.println("IP Address : "+ ipString);
		
		//initialise database
		activeUsers = new ActiveUsers();
		
		//Login initialisation 
		login = new Login("User/" + ipString, ipString);
		
		//lancer thread ListenUsers 
		ListenUsers lu = new ListenUsers(login, activeUsers);
		lu.start();
		System.out.println("[Users] Thread ListenUsers started");
		
		
		SendUDP.send("[1BD]:" + login.toString(), InetAddress.getByName("255.255.255.255"), 20000, true);
	
		
		/** dans un thread lancer ça
		 * System.out.println("[User]: Establishing connectino i.e opening listening server");
			//Establish connection 
			ThreadServer t = new ThreadServer();
		 */
	}
	
	public boolean changeUsername(String username) throws UnknownHostException, IOException {
		//verify that username is not taken
		boolean changed = false;
		if (!(activeUsers.hasValue(username))){
			login.setLogin(username);
			SendUDP.send("[UAU]:"+login.toString(), InetAddress.getByName("255.255.255.255"), 20000, true);
			changed = true;
		} 
		
		return changed;
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
