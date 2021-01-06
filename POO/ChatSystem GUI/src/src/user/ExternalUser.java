package src.user;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import src.communications.SendUDP;
import src.communications.WebSocketClient;
import src.database.ActiveUsersDB;
import src.database.MessagesDB;

public class ExternalUser {

	private Login login; 
	private ActiveUsersDB activeUsers;
	private InetAddress ip;
	private String ipString; 
	private MessagesDB messageDB;
	
	private WebSocketClient endpoint;
	private Session session;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	//private final static String IP = "192.168.1.1";
	
	public ExternalUser() {
		this.settingIPInfo();
		this.initialisation();
		this.websocketThread();
		this.sendDefaultUsermane();
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
        //this.ipString = IP;
		//System.out.println("IP Address : "+ ipString);
	}
	
	private void initialisation() {
		this.activeUsers = new ActiveUsersDB();	
		this.messageDB = new MessagesDB();
		activeUsers.dropUsers();
		this.login = new Login("User/" + this.ipString, this.ipString);
	}
	
	private void sendDefaultUsermane() {
		try {
			this.endpoint.sendMessage("[NewUser]:" + this.login.getIp() + ":" + this.login.getLogin() + ":" + "ExternalUsers");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean changeUsername(String username) throws UnknownHostException, IOException {
		//verify that username is not taken
		boolean changed = false;
		if (!(activeUsers.hasValue(username))){
			login.setLogin(username);
			String message = "[UserUpdate]:" + this.ipString + ":" + username + ":" + "ExternalUsers";
			this.endpoint.sendMessage(message);
			changed = true;
		} 
		
		return changed;
	}
	
	Runnable pingWebsocket = new Runnable() {
        public void run() {
            endpoint.pingServer(ByteBuffer.wrap(("This is a ping").getBytes()));
        }
	};
	
	private void websocketThread() {
		
		this.endpoint = new WebSocketClient(); 
		
		
 		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		
		try {	
			this.session = container.connectToServer(endpoint, new URI("ws://localhost:8080/ChatSystemWebSocket/hello"));
		} catch (DeploymentException | IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		scheduler.scheduleAtFixedRate(pingWebsocket, 0, 5, TimeUnit.SECONDS);
		
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
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		@SuppressWarnings("unused")
		ExternalUser u = new ExternalUser();
	}
	
}
