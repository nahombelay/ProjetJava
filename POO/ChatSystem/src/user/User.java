package user;

import java.io.IOException;
import java.net.*;
import java.util.*;

import agent.*;
import communications.SendUDP;
import database.ActiveUsers;
import database.ActiveUsersDB;
import database.MessagesDB;

public class User {
	
	private Login login; 
	private ActiveUsersDB activeUsers;
	private InetAddress ip;
	private String ipString; 
	private ServerSocket mainSocket;
	private MessagesDB messageDB;

	public User() {
	
		this.settingIPInfo();
		this.initialisation();
		this.threadUDP();
		this.threadTCP();
	
	}
	
	/**
	 * Getting related info about user's IP @
	 */
	private void settingIPInfo() {
		//get IP address       
        this.ip = this.getLocalAddress();
        this.ipString = ip.toString().substring(1);
		//System.out.println("IP Address : "+ ipString);
	}
	
	/**
	 * Initialise activeUsers hashtable
	 * Login initialisation with default username
	 */
	private void initialisation() {
		this.activeUsers = new ActiveUsersDB();	
		this.messageDB = new MessagesDB();
		activeUsers.dropUsers();
		this.login = new Login("User/" + this.ipString, this.ipString);
	}
	
	/**
	 * Each new info about a user goes throught this thread
	 */
	private void threadUDP() {
		//lancer thread ListenUsers 
		ListenUsers lu = new ListenUsers(login, activeUsers);
		lu.start();
		System.out.println("[Users] Thread ListenUsers started");
		
		try {
			SendUDP.send("[1BD]:" + login.toString(), InetAddress.getByName("255.255.255.255"), 20000, true);
			System.out.println("[1BD]:" + login.toString());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Manage all conversation of the user
	 */
	private void threadTCP() {
		MainSocket mainSock = new MainSocket();
		this.mainSocket = mainSock.getSocketServeur();
		ListenSocket listenSock = new ListenSocket(mainSocket, messageDB);
		listenSock.start();
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
		@SuppressWarnings("unused")
		User u = new User();
	}
}
