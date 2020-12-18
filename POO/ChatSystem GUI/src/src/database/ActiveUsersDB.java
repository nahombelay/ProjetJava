package src.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ActiveUsersDB extends UserDB {
	
	private Connection c = null;
	private Statement stm = null;
	private PreparedStatement pstm = null;
	private ResultSet rs = null;
	private int rowsModified = 0;
	//private String table = "";

	public ActiveUsersDB() {
		super();
		table = "ActiveUsers";
	}
	
	@Override
	public void closeConnection() {
		try {
			c.close();
			this.dropUsers();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * public static void main (String argv[]) { ActiveUsersDB bd = new
	 * ActiveUsersDB(); //bd.printUsers(); bd.dropUsers();
	 * 
	 * System.out.println("-----------"); bd.addUser("123.123.123.123", "toto");
	 * bd.printUsers();
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
