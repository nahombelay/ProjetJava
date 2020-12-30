package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
			//Class.forName("com.mysql.cj.jdbc.Driver");
			c = DriverManager.getConnection(url, username, password);
			System.out.println("[" + this.getClass().toString() + "] Connection OK");
	
		} catch ( SQLException  e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		System.out.println(db.getSessionId("1.1.1.1", "InternalUsers"));
		
	}
}
