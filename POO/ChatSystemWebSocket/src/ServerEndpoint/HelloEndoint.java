package ServerEndpoint;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.PongMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/hello")
public class HelloEndoint {
	
	private Session session;
	
	@OnOpen
	public void onCreateSession(Session session) {
		this.session = session;
	}
	
	@OnMessage
	public void onTextMessage(String message, Session session) {
		System.out.println("Message = " + message + " par session: " + session.getId());
		
		if(this.session != null & this.session.isOpen()) {
			try {
				this.session.getBasicRemote().sendText("From Server: " + message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@OnMessage
	public void onPong(PongMessage pongMessage) {
		
	}
	@OnClose
	public void onClose() {
		System.out.println("Closed");
	}
	
}
