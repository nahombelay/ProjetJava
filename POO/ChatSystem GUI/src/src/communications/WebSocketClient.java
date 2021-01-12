package src.communications;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.jsoup.select.Elements;

import src.database.ActiveUsersDB;
import src.scrapper.HTMLscrapper;

@ClientEndpoint
public class WebSocketClient extends Endpoint {

	private Session session;
	
	@Override
	public void onOpen(Session session, EndpointConfig arg1) {
		this.session = session;
		
		this.session.addMessageHandler(new MessageHandler.Whole<String>() {

			@Override
			public void onMessage(String message) {
				// TODO Auto-generated method stub
				System.out.println("!!!!!!!!! retrieved: " + message);
				ActiveUsersDB usersDatabase = new ActiveUsersDB();
				System.out.println(message.substring(0,7));
				if (message.substring(0,6).equals("<table")) {
					HTMLscrapper hs = new HTMLscrapper(message);
					Elements e = hs.getTable();
					hs.addRowsToDatabase(e);
				}
				
				else if (message.substring(0, ("[UserUpdate]").length()).equals("[UserUpdate]")) {
					String [] formatedMessage = message.split(":");
					String ip = formatedMessage[1];
					String username = formatedMessage[2];
					String status = formatedMessage[3];
					usersDatabase.updateUser(ip, username);
					try {
						usersDatabase.changeStatus(username, status);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					
					//TODO: if message is textmessage
				else {
					//[FORWARDED]:sourceip:destinationip:internal/external:message
					//[FORWARDED]:sender:message
					String [] formatedMessage = message.split(":");
					String sender = formatedMessage[1];
					String text = formatedMessage[2];
					
					//TODO: notify chat controller
					
					
				}
				
			}
		});
	}
	
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}
	
	public void pingServer(ByteBuffer buffer) {
		try {
			this.session.getBasicRemote().sendPing(buffer);
		} catch (IllegalArgumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@OnMessage
//	public void onTextMessage(String message, Session session) {
//		ActiveUsersDB usersDatabase = new ActiveUsersDB();
//		System.out.println("received response");
//		if (message.substring(0,6).equals("<table")) {
//			HTMLscrapper hc = new HTMLscrapper(message);
//			Elements e = hc.getTable();
//			hc.addRowsToDatabase(e);
//		}
//		
//		else if (message.substring(0, 12).equals("[UserUpdate]")) {
//			String [] formatedMessage = message.split(":");
//			String ip = formatedMessage[1];
//			String username = formatedMessage[2];
//			String status = formatedMessage[3];
//			usersDatabase.addUser(ip, username);
//			try {
//				usersDatabase.changeStatus(username, status);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		//TODO: if message is textmessage
//		
//	
//	}
	
	public static void main (String[] argv) {
		WebSocketClient endpoint = new WebSocketClient(); 
		Session sess = null;
		
 		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		
		try {	
			sess = container.connectToServer(endpoint, new URI("ws://localhost:8080/ChatSystemWebSocket/ListenUsers"));
		} catch (DeploymentException | IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			endpoint.sendMessage("[NewUser]:11.1.2.1:lolo:Online:ExternalUsers");
			System.out.println("New user sent");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sess.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
