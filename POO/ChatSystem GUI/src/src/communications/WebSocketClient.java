package src.communications;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class WebSocketClient extends Endpoint{

	private Session session;
	
	@Override
	public void onOpen(Session arg0, EndpointConfig arg1) {
		this.session = arg0;
		
		this.session.addMessageHandler(new MessageHandler.Whole<String>() {

			@Override
			public void onMessage(String arg0) {
				// TODO Auto-generated method stub
				System.out.println("!!!!!!!!! retrieved: " + arg0);
				
			}
		});
	}
	
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}
	
	public static void main (String[] argv) {
		WebSocketClient endpoint = new WebSocketClient(); 
		Session sess = null;
		
 		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		
		try {	
			sess = container.connectToServer(endpoint, new URI("ws://localhost:8080/ChatSystemWebSocket/hello"));
		} catch (DeploymentException | IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			endpoint.sendMessage("[NewUser]:1.1.2.1:titi:online:InternalUsers");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			sess.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
