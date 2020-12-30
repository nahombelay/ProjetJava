import java.io.IOException;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


public class ProgrammaticEchoServer extends Endpoint {
	
@Override
	public void onOpen(Session session, EndpointConfig endpointConfig) {
		final Session mySession = session; 
		mySession.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String text) {
		try {
		mySession.getBasicRemote().sendText("I got this ("
		                                  +text + ") so I am sending it back !");
		                } catch (IOException ioe) {
		System.out.println("oh dear, something went wrong trying to send the message back: " + ioe.getMessage());
		} }
}); }
}
