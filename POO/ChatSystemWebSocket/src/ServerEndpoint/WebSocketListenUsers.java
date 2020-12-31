package ServerEndpoint;



import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.PongMessage;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import com.database.UsersDatabaseServer;

@ServerEndpoint("/ListenUsers")
public class WebSocketListenUsers {	
	private Session session;
	private static Map<String,Session> clients = 
		    Collections.synchronizedMap(new HashMap<String,Session>());

	
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
			UsersDatabaseServer db = new UsersDatabaseServer();
			
			String [] formatedMessage = message.split(":");;
			
			if (formatedMessage[0].equals("[NewUser]")) {
				//format: [NewUser]:ip:username:status:internal/external
				db.addUser(formatedMessage[1], formatedMessage[2], formatedMessage[3], session.getId(), formatedMessage[4]);
				try {
					this.session.getBasicRemote().sendText("From Server: User has been added");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String userInfo = "[NewUser]" +formatedMessage[1] + ":" + formatedMessage[2] + ":" + formatedMessage[3];
				broadcastUserInfo(db, userInfo, formatedMessage[4]);
				
				String HTMLTable = usersHTMLTable(db, formatedMessage[4]);
				try {
					System.out.println(HTMLTable);
					session.getBasicRemote().sendText(HTMLTable);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else if (formatedMessage[0].equals("[UserUpdate]")) {
				if (formatedMessage[3].contentEquals("offline")) {
					db.deleteUser(formatedMessage[1], formatedMessage[2], session.getId(), formatedMessage[4]);
				} else {
					db.updateUser(formatedMessage[1], formatedMessage[2], formatedMessage[3], session.getId(), formatedMessage[4]);
				}

				String userInfo = "[UserUpdate]" + formatedMessage[1] + ":" + formatedMessage[2] + ":" + formatedMessage[3];
				broadcastUserInfo(db, userInfo, formatedMessage[4]);
				
			} else if (formatedMessage[0].equals("[Forward]")) {
				System.out.println("Forwarding from " + formatedMessage[1] + " to " + formatedMessage[2]);
				String messageToBeForwarded = "[Forwarded]:" + formatedMessage[1] + ":" + formatedMessage[3];
				if (formatedMessage[4].equals("InternalUsers")) {
					forwardMessage(db, formatedMessage[2] , formatedMessage[1] , "ExternalUsers", messageToBeForwarded);
				} else {
					forwardMessage(db, formatedMessage[2] , formatedMessage[1] , "InternalUsers", messageToBeForwarded);
				}
				
			}
			
		}
	}
	
	@OnClose
	public void onClose(Session session) {
		System.out.println("Closed");
		clients.remove(session.getId(), session);
		
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
					Session session = clients.get(sessionId);
					try {
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
		String HTMLTable = "<table >\n" + 
				"			<tr>\n" + 
				"				<th>ip</th>\n" + 
				"				<th>username</th>\n" + 
				"				<th>status</th>\n" + 
				"			</tr>";
		Connection c = db.getConnection("ExternalUsers");
		ResultSet rs;
		try {
			rs = c.createStatement().executeQuery("SELECT * FROM ExternalUsers");

			while (rs.next()) {
				String ip = rs.getString(1);
				String username = rs.getString(2);
				String status = rs.getString(3);
				
				HTMLTable += ("<tr>");
				HTMLTable += ("<td> " + ip + "</td>");
				HTMLTable += ("<td> " + username + "</td>");
				HTMLTable += ("<td> " + status + "</td>");
				HTMLTable += ("</tr>");
			}

			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		if (table.equals("ExternalUsers")) {
			c = db.getConnection("InternalUsers");
			try {
				rs = c.createStatement().executeQuery("SELECT * FROM ExternalUsers");

				while (rs.next()) {
					String ip = rs.getString(1);
					String username = rs.getString(2);
					String status = rs.getString(3);
					
					HTMLTable += ("<tr>");
					HTMLTable += ("<td> " + ip + "</td>");
					HTMLTable += ("<td> " + username + "</td>");
					HTMLTable += ("<td> " + status + "</td>");
					HTMLTable += ("</tr>");
				}
				
				c.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return HTMLTable;
	}
	
}
