package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class MessagesDB  {
	
	private  Connection c = null;
	private  PreparedStatement pstm = null;
	@SuppressWarnings("unused")
	private  int rowsModified = 0;
	protected String table = "";
	
	public MessagesDB() {
		
		String url = "jdbc:sqlite:SQLiteDatabases/usersDatabase.db";
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
}
