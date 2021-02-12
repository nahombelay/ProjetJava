package ChatSystem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class MessagesDB  {
	
	private  Connection c = null;
	private  PreparedStatement pstm = null;
	private Statement stm = null;
	@SuppressWarnings("unused")
	private  int rowsModified = 0;
	protected String table = "";
	
	public MessagesDB() {
		
		String url = "jdbc:sqlite:src/main/resources/ChatSystem/SQLiteDatabases/usersDatabase.db";
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(url);
			System.out.println("[" + this.getClass().toString() + "] Connection OK");

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		table = "Messages";
	}
	
	public void addMessage(String ipDest, boolean isSender, String text) {
		String query = "INSERT INTO " + table + " (peer, isSender, text) VALUES (?,?,?);";
		
		try {
			pstm = c.prepareStatement(query);
			pstm.setString(1, ipDest);
			pstm.setBoolean(2, isSender);
			pstm.setString(3, text);
			rowsModified = pstm.executeUpdate();
			pstm.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteMessage(String ipDest, boolean isSender, String text) {
		String query = "DELETE FROM " + table + " WHERE peer = ? AND isSender = ? AND text = ?;";
		
		try {
			this.pstm = c.prepareStatement(query);
			this.pstm.setString(1, ipDest);
			this.pstm.setBoolean(2, isSender);
			this.pstm.setString(3, text);
			this.rowsModified = pstm.executeUpdate();
			this.pstm.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clearMessages() {
		try {
			this.stm = c.createStatement();
			this.rowsModified = stm.executeUpdate("DELETE FROM " + table + ";");

			stm.close();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<ArrayList<String>> getMessages(String ipDest) {
		ArrayList<String> textArray = new ArrayList<String>();
		ArrayList<String> timeArray = new ArrayList<String>();
		ArrayList<String> posArray = new ArrayList<String>();
		String query = "SELECT text, timestamp, isSender FROM Messages WHERE peer=?;";
		ResultSet rs = null;
		String isSender = null;
		try {
			pstm = c.prepareStatement(query);
			pstm.setString(1, ipDest);
			rs = pstm.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					String text = rs.getString(1);
					String time = rs.getString(2);
					
					if (rs.getBoolean(3) == true) {
						isSender = "true";
					} else {
						isSender = "false";
					}
					textArray.add(text);
					timeArray.add(time);
					posArray.add(isSender);
				}
				rs.close();
				pstm.close();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(textArray.size() == 0) {
			return null;
		} else {
			ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
			array.add(textArray);
			array.add(timeArray);
			array.add(posArray);
			return array;
		}
		
	}
	
	public static void main(String[] args) {  
		new MessagesDB().clearMessages();
	} 
	
	
	
}
