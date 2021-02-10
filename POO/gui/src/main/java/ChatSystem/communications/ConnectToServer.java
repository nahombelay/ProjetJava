package ChatSystem.communications;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

public class ConnectToServer {
	private WebSocketClient endpoint;
	public WebSocketClient getEndpoint() {
		return endpoint;
	}

	public WebSocketContainer getContainer() {
		return container;
	}

	private Session session;
	private WebSocketContainer container;
	
	public ConnectToServer() {
		//TODO: verifier qu'on n'utilise pas
		endpoint = new WebSocketClient(null, null); 
 		container = ContainerProvider.getWebSocketContainer();
 		
 		try {	
			session = container.connectToServer(endpoint, new URI("ws://localhost:8080/ChatSystemWebSocket/ListenUsers"));
			System.out.println("Connected to endpoint");
		} catch (DeploymentException | IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main (String[] argv) {
		ConnectToServer c = new ConnectToServer();
			try {
				c.getEndpoint().sendMessage("[UserUpdate]:1.1.1.1:tit:offline:InternalUsers");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
	
	}

	public Session getSession() {
		return session;
	}
}
