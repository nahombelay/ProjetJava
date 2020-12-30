package src.user;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import src.database.ActiveUsersDB;
import src.database.MessagesDB;

public class ExternalUser {

	private Login login; 
	private ActiveUsersDB activeUsers;
	private InetAddress ip;
	private String ipString; 
	private MessagesDB messageDB;
	
	public ExternalUser() {
		this.settingIPInfo();
		this.initialisation();
		//start http threads
	}
	
	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public MessagesDB getMessageDB() {
		return messageDB;
	}

	public ActiveUsersDB getActiveUsers() {
		return activeUsers;
	}
	
	private void settingIPInfo() {
		//get IP address       
        this.ip = this.getLocalAddress();
        this.ipString = ip.toString().substring(1);
		//System.out.println("IP Address : "+ ipString);
	}
	
	private void initialisation() {
		this.activeUsers = new ActiveUsersDB();	
		this.messageDB = new MessagesDB();
		activeUsers.dropUsers();
		this.login = new Login("User/" + this.ipString, this.ipString);
	}
	
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
	
}
