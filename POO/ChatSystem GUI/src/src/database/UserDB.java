package src.database;

import java.sql.*;

//UserDB will be used to save all the users our user has interacted with in the past but also 
//keep track of all active users


public class UserDB {
	
	private Connection c = null;
	private Statement stm = null;
	private PreparedStatement pstm = null;
	private ResultSet rs = null;
	@SuppressWarnings("unused")
	private int rowsModified = 0;
	protected String table = "";
	
	public UserDB() {
		
		String url = "jdbc:sqlite:SQLiteDatabases/usersDatabase.db";
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
	}
	
	public void updateUser(String ip, String username) {
		String query = "UPDATE " + table + " SET username = ? WHERE ip = ?;";
		
		try {
			this.pstm = c.prepareStatement(query);
			this.pstm.setString(1, username);
			this.pstm.setString(2, ip);
			this.rowsModified = pstm.executeUpdate();
			
			this.pstm.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		System.out.println(query);
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
	
	public void closeConnection() {
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/*
	 * public static void main (String argv[]) { UserDB bd = new UserDB();
	 * //bd.printUsers();
	 * 
	 * System.out.println("-----------"); bd.addUser("123.123.123.123", "toto");
	 * bd.printUsers();
	 * 
	 * System.out.println("-----------");
	 * System.out.println(bd.hasKey("123.123.123.123"));
	 * 
	 * 
	 * System.out.println("-----------");
	 * 
	 * bd.updateUser("123.123.123.123", "titi");
	 * 
	 * bd.printUsers(); System.out.println("-----------");
	 * 
	 * bd.deleteUser("123.123.123.123", "titi"); bd.printUsers();
	 * 
	 * bd.dropUsers();
	 * 
	 * bd.closeConnection();
	 * 
	 * }
	 */
	 
	 

}
