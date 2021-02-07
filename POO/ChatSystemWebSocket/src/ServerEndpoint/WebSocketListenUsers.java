package ServerEndpoint;



import java.io.IOException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import com.database.UsersDatabaseServer;

@ServerEndpoint("/ListenUsers")
public class WebSocketListenUsers {	
	private Session session;
	private static Map<String,Session> clients = 
		    Collections.synchronizedMap(new HashMap<String,Session>());
	private UsersDatabaseServer db = new UsersDatabaseServer();
	
	@OnOpen
	public void onCreateSession(Session session) {
		this.session = session;
		clients.put(session.getId(),session);
		System.out.println("New User added to clients. SessionID = " + session.getId());
	}
	
	@OnMessage
	public void onTextMessage(String message, Session session) {
		
		if(this.session != null & this.session.isOpen()) {
			//System.out.println("Message = " + message + " par session: " + session.getId());
			
			
			String [] formatedMessage = message.split(":");
			String messageHeader = formatedMessage[0];
			int messageLength = formatedMessage.length;
			
			if (messageHeader.equals("[NewUser]")) {
				//format: [NewUser]:ip:username:status:internal/external
				//format: [NewUser]:ip:username:internal/external -> by default online
				String ipSource = formatedMessage[1];
				String usernameSource = formatedMessage[2];
				//String statusSource = formatedMessage[3];
				String statusSource = "Online";
				String typeSource = formatedMessage[3]; //internal or external
				String userInfo = "[NewUser]:" + ipSource + ":" + usernameSource + ":" + statusSource;
				
				if (db.usernameExists(usernameSource, typeSource)) {
					try {
						session.getBasicRemote().sendText("[InvalidUsername]");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					broadcastUserInfo(db, userInfo, formatedMessage[3]);
					
					String HTMLTable = usersHTMLTable(db, typeSource);
					try {
						//System.out.println(HTMLTable);
						session.getBasicRemote().sendText(HTMLTable);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					db.addUser(ipSource, usernameSource, "Online", session.getId(), typeSource);
				}
				
			} else if (messageHeader.equals("[UserUpdate]")) {
				//FORMAT: [UserUpdate]:ip:username:status:table
				
				String ipSource = formatedMessage[1];
				String usernameSource = formatedMessage[2];
				String statusSource = formatedMessage[3];
				String typeSource = formatedMessage[4]; //internal or external
				if (statusSource.equals("Offline")) {
					db.deleteUser(ipSource, usernameSource, session.getId(), typeSource);
				} else {
					db.updateUser(ipSource, usernameSource, statusSource, session.getId(), typeSource);
				}
				String userInfo = "[UserUpdate]:" + ipSource + ":" + usernameSource +  ":" + statusSource;
				broadcastUserInfo(db, userInfo, typeSource);
				
			} else if (messageHeader.equals("[Forward]")) {
				//[FORWARD]:ipSource:ipDest:Table:message
				
				String ipSource = formatedMessage[1];
				String ipDest = formatedMessage[2];
				String typeSource = formatedMessage[3];
				String text;
				if (messageLength == 5) {
					text = formatedMessage[4];
				} else {
					text = String.join(":", Arrays.asList(formatedMessage).subList(4, messageLength));
				}
				
				System.out.println("Forwarding from " + ipSource + " to " + ipDest);
				//[Forwarded]:ipSource:message
				String messageToBeForwarded = "[Forwarded]:" + ipSource + ":" + text;
				if (typeSource.equals("InternalUsers")) {
					forwardMessage(db, ipDest , ipSource , "ExternalUsers", messageToBeForwarded);
				} else {
					if (db.getSessionId(ipDest, "ExternalUsers") != null) {
						forwardMessage(db, ipDest , ipSource , "ExternalUsers", messageToBeForwarded);
					} else {
						forwardMessage(db, ipDest , ipSource , "InternalUsers", messageToBeForwarded);
					}
					
				}
				
			}
			
		}
	}
	
	@OnClose
	public void onClose(Session session) {
		System.out.println("Closed");
		String sessionID = session.getId();
		clients.remove(sessionID, session);
		if ((db.isInTable(sessionID, "InternalUsers")) || (db.isInTable(sessionID, "ExternalUsers"))) {
			db.deleteUser(sessionID);
		} 
		
	}
	
	@OnError
	public void onError(Session session, Throwable t) {
		System.out.println(t.toString());
	}
	

	
	public void broadcastUserInfo(UsersDatabaseServer db, String userInfo, String table) {
		String currentSessionId = this.session.getId();
		if (table.equals("InternalUsers")) {
			Connection connection = db.getConnection("ExternalUsers");
			try {
				Statement s = connection.createStatement();
				ResultSet rset = s.executeQuery("SELECT sessionID FROM ExternalUsers");
				
				while (rset.next()) {
					String sessionId = rset.getString(1);
					if (!sessionId.equals(currentSessionId)){
						Session session = clients.get(sessionId);
						session.getBasicRemote().sendText(userInfo);
					}
				}
				
				s.close();
				rset.close();
				
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			for (String sessionId : clients.keySet()) {
				if (!sessionId.equals(currentSessionId)){
					try {
						Session session = clients.get(sessionId);
						session.getBasicRemote().sendText(userInfo);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
			}
		}
	}
	
	public void forwardMessage(UsersDatabaseServer db, String destinationIP, String sourceIP, String table, String message) {
		String sessionId = db.getSessionId(destinationIP, table);
		Session destinationSession = clients.get(sessionId);
		try {
			destinationSession.getBasicRemote().sendText(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String usersHTMLTable(UsersDatabaseServer db, String table) {
		String HTMLTable = "<table class=\"Users Table\">\n" + 
				"			<tr>\n" + 
				"				<th>ip</th>\n" + 
				"				<th>username</th>\n" + 
				"				<th>status</th>\n" + 
				"			</tr>";

		ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
		array = db.createTable("ExternalUsers");
		
		if (array != null) {
			ArrayList<String> ipArray = array.get(0);
			ArrayList<String> usernameArray = array.get(1);
			ArrayList<String> statusArray = array.get(2);
			int size = ipArray.size();
			for (int i = 0 ; i < size ; i++) {
				HTMLTable += ("<tr>");
				HTMLTable += ("<td> " + ipArray.get(i) + "</td>");
				HTMLTable += ("<td> " + usernameArray.get(i) + "</td>");
				HTMLTable += ("<td> " + statusArray.get(i) + "</td>");
				HTMLTable += ("</tr>");
			}
		}

		if (table.equals("ExternalUsers")) {
			array = db.createTable("InternalUsers");
			
			if (array != null) {
				ArrayList<String> ipArray = array.get(0);
				ArrayList<String> usernameArray = array.get(1);
				ArrayList<String> statusArray = array.get(2);
				int size = ipArray.size();
				for (int i = 0 ; i < size ; i++) {
					HTMLTable += ("<tr>");
					HTMLTable += ("<td> " + ipArray.get(i) + "</td>");
					HTMLTable += ("<td> " + usernameArray.get(i) + "</td>");
					HTMLTable += ("<td> " + statusArray.get(i) + "</td>");
					HTMLTable += ("</tr>");
				}
			}
		}

		return HTMLTable;
	}
	
}
