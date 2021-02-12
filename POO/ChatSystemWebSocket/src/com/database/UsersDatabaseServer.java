package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
public class UsersDatabaseServer {
	
	private Connection c = null;
	private Statement stm = null;
	private PreparedStatement pstm = null;
	private ResultSet rs = null;
	@SuppressWarnings("unused")
	private int rowsModified = 0;
	private String username = "tp_servlet_016";
    private String password = "aiTooN5b";
	
	public UsersDatabaseServer() {
		
		String url = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/tp_servlet_016?useSSL=false";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection(url, username, password);
	
		} catch ( SQLException | ClassNotFoundException  e) {
			e.printStackTrace();
		}
	}
	
	public void addUser(String ip, String username, String status, String sessionID, String table) {
		String query = "INSERT INTO " + table + " (ip, username, status, sessionID) VALUES (?,?,?,?);";
		
		try {
			this.pstm = c.prepareStatement(query);
			this.pstm.setString(1, ip);
			this.pstm.setString(2, username);
			this.pstm.setString(3, status);
			this.pstm.setString(4, sessionID);
			this.rowsModified = pstm.executeUpdate();
			this.pstm.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteAll() {
		try {
			this.stm = c.createStatement();
			this.rowsModified = stm.executeUpdate("DELETE FROM InternalUsers" + ";");

			stm.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			this.stm = c.createStatement();
			this.rowsModified = stm.executeUpdate("DELETE FROM ExternalUsers" + ";");

			stm.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteUser(String ip, String username, String sessionID, String table) {
		String query = "DELETE FROM " + table + " WHERE ip = ? OR username = ? OR sessionID = ?;";
		
		try {
			this.pstm = c.prepareStatement(query);
			this.pstm.setString(1, ip);
			this.pstm.setString(2, username);
			this.pstm.setString(3, sessionID);
			this.rowsModified = pstm.executeUpdate();
			this.pstm.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteUser(String sessionID) {

		String table = "";
		if (isInTable(sessionID, "InternalUsers")) {
			table = "InternalUsers";
		} else if (isInTable(sessionID, "ExternalUsers")) {
			table = "ExternalUsers";
		}
		
		String query = "DELETE FROM " + table + " WHERE sessionID = ?;";
		
		try {
			this.pstm = c.prepareStatement(query);
			this.pstm.setString(1, sessionID);
			this.rowsModified = pstm.executeUpdate();
			this.pstm.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean usernameExists(String username, String table) {
		boolean isPresent;
		int count = 0;
		String query = "SELECT COUNT(1) FROM " + table + " WHERE username = '" + username + "';";

		try {
			this.stm = c.createStatement();
			this.rs = stm.executeQuery(query);
			
			while(rs.next()) {
				count = rs.getInt(1);
			}
			
			stm.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		isPresent = (count == 1);
		return isPresent;
	}
	
	public boolean isInTable(String sessionID, String table) {
		boolean isPresent;
		int count = 0;
		String query = "SELECT COUNT(1) FROM " + table + " WHERE sessionID = '" + sessionID + "';";

		try {
			this.stm = c.createStatement();
			this.rs = stm.executeQuery(query);
			
			while(rs.next()) {
				count = rs.getInt(1);
			}
			
			stm.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		isPresent = (count == 1);
		return isPresent;
	}
	public void updateUser(String ip, String username, String status, String sessionID, String table) {
		String query = "UPDATE " + table + " SET username = ?, status = ? WHERE ip = ? OR sessionID = ?;";
		
		try {
			this.pstm = c.prepareStatement(query);
			this.pstm.setString(2, status);
			this.pstm.setString(1, username);
			this.pstm.setString(3, ip);
			this.pstm.setString(4, sessionID);
			this.rowsModified = pstm.executeUpdate();
			
			this.pstm.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<ArrayList<String>> createTable(String Table){
		ArrayList<String> ip = new ArrayList<String>();
		ArrayList<String> username = new ArrayList<String>();
		ArrayList<String> status = new ArrayList<String>();
		String query = "SELECT ip, username, status FROM " + Table + " ;";
		
		try {
			stm = c.createStatement();
			rs = stm.executeQuery(query);
			if (rs != null) {
				while (rs.next()) {					
					ip.add(rs.getString(1));
					username.add(rs.getString(2));
					status.add(rs.getString(3));
				}
				rs.close();
				stm.close();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ip.size() == 0) {
			return null;
		} else {
			ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
			array.add(ip);
			array.add(username);
			array.add(status);
			return array;
		}
		
	}
	
	public String getSessionId(String ip, String table) {
		String sessionID = null;
		String query = "SELECT sessionID FROM " + table + " WHERE ip = '" + ip + "' ;" ;
		try {
			this.stm = c.createStatement();
			this.rs = stm.executeQuery(query);
			
			while (rs.next()) {
				sessionID = rs.getString(1);
			}
			
			stm.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sessionID;
	}
	public Connection getConnection(String table) {
		return this.c;
	}
	public static void main (String [] agrv) {
		UsersDatabaseServer db = new UsersDatabaseServer();
		db.deleteAll();
	}
}
