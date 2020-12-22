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
	
	public UsersDatabaseServer() {
		String url = "jdbc:sqlite://localhost:3306/UsersDatabaseServer.db";
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(url);
			System.out.println("[" + this.getClass().toString() + "] Connection OK");
	
		} catch ( SQLException | ClassNotFoundException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addUser(String ip, String username, String status,  String table, String URL) {
		String query = "INSERT INTO " + table + " (ip, username, status, URL) VALUES (?,?,?,?);";
		
		try {
			this.pstm = c.prepareStatement(query);
			this.pstm.setString(1, ip);
			this.pstm.setString(2, username);
			this.pstm.setString(3, status);
			this.pstm.setString(4, URL);
			this.rowsModified = pstm.executeUpdate();
			this.pstm.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteUser(String ip, String username,  String table) {
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
	
	public void updateUser(String ip, String username, String status, String table) {
		String query = "UPDATE " + table + " SET username = ?, status = ? WHERE ip = ?;";
		
		try {
			this.pstm = c.prepareStatement(query);
			this.pstm.setString(2, status);
			this.pstm.setString(1, username);
			this.pstm.setString(3, ip);
			this.rowsModified = pstm.executeUpdate();
			
			this.pstm.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main (String [] agrv) {
		UsersDatabaseServer db = new UsersDatabaseServer();
		//db.deleteUser("1.1.1.1", "toto", "InternalUsers");
		
	}
}
