package ChatSystem.database;

import java.sql.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import ChatSystem.gui.ChatController;
import ChatSystem.user.Login;

//UserDB will be used to save all the users our user has interacted with in the past but also 
//keep track of all active users


public class UserDB {
	
	private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();
	
	protected Connection c = null;
	private Statement stm = null;
	private PreparedStatement pstm = null;
	private ResultSet rs = null;
	@SuppressWarnings("unused")
	private int rowsModified = 0;
	protected String table = "";
	
	public UserDB() {
		
		String url = "jdbc:sqlite:src/main/resources/ChatSystem/SQLiteDatabases/usersDatabase.db";
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(url);
			System.out.println("[" + this.getClass().toString() + "] Connection OK");

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table = "Users";
	}
	
	public void printUsers() {
		try {
			this.stm = c.createStatement();
			this.rs = stm.executeQuery("SELECT * FROM " + table + ";");
			
			while (rs.next()) {
				String ip = rs.getString(1);
				String username = rs.getString(2);
				
				System.out.println(ip + " : " + username);
			}
			
			stm.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void changeStatus(String username, String status) throws Exception {
		if (status.equals("Online") || status.equals("Do Not Disturb") || status.equals("Offline")) {
			String query = "UPDATE " + table + " SET status = ? WHERE username = ?;";
			try {
				this.pstm = c.prepareStatement(query);
				this.pstm.setString(1, status);
				this.pstm.setString(2, username);
				this.rowsModified = pstm.executeUpdate();
				this.pstm.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			throw new Exception("Wrong Status");
		}
		notifyListeners(ChatController.class, "changeStatus", username, status);
	}
	
	public String getStatus(String ip) {
		String query = "SELECT status FROM " + table + " WHERE ip = '" + ip + "';";
		System.out.println(query);
		String status = null;
		try {
			this.stm = c.createStatement();
			this.rs = stm.executeQuery(query);
			status = rs.getString(1);
			stm.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return status;
	}
	
	public void addUser(String ip, String username) {
		String query = "INSERT INTO " + table + " (ip, username) VALUES (?,?);";
		
		try {
			this.pstm = c.prepareStatement(query);
			this.pstm.setString(1, ip);
			this.pstm.setString(2, username);
			this.rowsModified = pstm.executeUpdate();
			this.pstm.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyListeners(ChatController.class, "newUser", ip, username);
	}
	
	public void deleteUser(String ip, String username) {
		String query = "DELETE FROM " + table + " WHERE ip = ? OR username = ?;";
		
		try {
			this.pstm = c.prepareStatement(query);
			this.pstm.setString(1, ip);
			this.pstm.setString(2, username);
			this.rowsModified = pstm.executeUpdate();
			this.pstm.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyListeners(ChatController.class, "deleteUser", ip, username);
	}
	
	public void updateUser(String ip, String newUsername) {
		String oldUsername = getCurrentUsername(ip);
		String query = "UPDATE " + table + " SET username = ? WHERE ip = ?;";
		
		try {
			this.pstm = c.prepareStatement(query);
			this.pstm.setString(1, newUsername);
			this.pstm.setString(2, ip);
			this.rowsModified = pstm.executeUpdate();
			
			this.pstm.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyListeners(ChatController.class, "updateUser", oldUsername, newUsername);
	}
	
	public String getCurrentUsername(String ip) {
		String query = "SELECT username FROM " + table + " WHERE ip = '" + ip + "';";
		String username = null;
		try {
			this.stm = c.createStatement();
			this.rs = stm.executeQuery(query);
			username = rs.getString(1);
			stm.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return username;
	}
	
	public String getCurrentIp(String username) {
		String query = "SELECT ip FROM " + table + " WHERE username = '" + username + "';";
		String ip = null;
		try {
			this.stm = c.createStatement();
			this.rs = stm.executeQuery(query);
			ip = rs.getString(1);
			stm.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ip;
	}

	public boolean hasValue(String username) {
		String query = "SELECT COUNT (1) FROM " + table + " WHERE username = '" + username + "' ;";
		boolean rep = false;
		try {
			this.stm = c.createStatement();
			this.rs = stm.executeQuery(query);
			rep = rs.getString(1).equals("1");
			stm.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rep;
	}
	
	public boolean hasKey(String ip) {
		//String query = "SELECT COUNT (1) FROM " + table + " WHERE ip = '" + ip + "' ;";
		String query = "SELECT COUNT (1) FROM " + table + " WHERE ip = '" + ip + "';";
		///System.out.println(query);
		boolean rep = false;
		try {
			this.stm = c.createStatement();
			this.rs = stm.executeQuery(query);
			rep = rs.getString(1).equals("1");
			stm.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rep;
	}
	
	
	public void dropUsers() {
		try {
			this.stm = c.createStatement();
			this.rowsModified = stm.executeUpdate("DELETE FROM " + table + ";");

			stm.close();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Login> getAllUsers() throws ClassNotFoundException, SQLException {
	    this.stm = c.createStatement();
	    String sql = "Select * From " + table;
	    this.rs = stm.executeQuery(sql);
	    ArrayList<Login> allUsers = new ArrayList<>();
	    while (rs.next()) {
	        Login login = new Login(rs.getString("username"), rs.getString("ip"));
	        allUsers.add(login);
	    }
	    this.stm.close();
	    this.rs.close();
	    return allUsers;
	}
	
	public ArrayList<Login> getOnlineUsers() throws ClassNotFoundException, SQLException {
	    this.stm = c.createStatement();
	    String sql = "Select * From " + table + " WHERE status = 'Online'";
	    this.rs = stm.executeQuery(sql);
	    ArrayList<Login> allUsers = new ArrayList<>();
	    while (rs.next()) {
	        Login login = new Login(rs.getString("username"), rs.getString("ip"));
	        allUsers.add(login);
	    }
	    return allUsers;
	}
	

	public ArrayList<Login> getDNDUsers() throws ClassNotFoundException, SQLException {
	    this.stm = c.createStatement();
	    String sql = "Select * From " + table + " WHERE status = 'Do Not Disturb'";
	    this.rs = stm.executeQuery(sql);
	    ArrayList<Login> allUsers = new ArrayList<>();
	    while (rs.next()) {
	        Login login = new Login(rs.getString("username"), rs.getString("ip"));
	        allUsers.add(login);
	    }
	    this.stm.close();
	    this.rs.close();
	    return allUsers;
	}
	

	public ArrayList<Login> getOfflineUsers() throws ClassNotFoundException, SQLException {
	    this.stm = c.createStatement();
	    String sql = "Select * From " + table + " WHERE status = 'Offline'";
	    this.rs = stm.executeQuery(sql);
	    ArrayList<Login> allUsers = new ArrayList<>();
	    while (rs.next()) {
	        Login login = new Login(rs.getString("username"), rs.getString("ip"));
	        allUsers.add(login);
	    }
	    this.stm.close();
	    this.rs.close();
	    return allUsers;
	}
	
	public void closeConnection() {
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private synchronized void notifyListeners(Object object, String property, String ip, String username) {
        for (PropertyChangeListener name : listener) {
            name.propertyChange(new PropertyChangeEvent(this, property, ip, username));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listener.add(newListener);
    }
	
	
//	  public static void main (String argv[]) { UserDB bd = new UserDB();
//	  //bd.printUsers();
//	  
//	  System.out.println("-----------"); bd.addUser("123.123.123.123", "toto");
//	  bd.printUsers();
//	  
//	  System.out.println("-----------");
//	  System.out.println(bd.hasKey("123.123.123.123"));
//	  
//	  
//	  System.out.println("-----------");
//	  
//	  bd.updateUser("123.123.123.123", "titi");
//	  
//	  bd.printUsers(); System.out.println("-----------");
//	  
//	  bd.deleteUser("123.123.123.123", "titi"); bd.printUsers();
//	  
//	  bd.dropUsers();
//	  
//	  bd.closeConnection();
//	  
//	  }
	 
	 
	 

}
