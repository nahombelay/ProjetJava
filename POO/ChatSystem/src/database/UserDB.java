package database;

import java.sql.*;

//UserDB will be used to save all the users our user has interacted with in the past but also 
//keep track of all active users


public class UserDB {
	
	Connection c = null;
	Statement stm = null;
	PreparedStatement pstm = null;
	ResultSet rs = null;
	int rowsModified = 0;
	
	public UserDB() {
		
		String url = "jdbc:sqlite:SQLiteDatabases/usersDatabase.db";
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(url);
			System.out.println("[UserDB] Connection OK");

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printUsers () {
		try {
			this.stm = c.createStatement();
			this.rs = stm.executeQuery("SELECT * FROM Users");
			
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
		String query = "INSERT INTO Users (ip, username) VALUES (?,?);";
		
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
		String query = "DELETE FROM Users WHERE ip = ? AND username = ?;";
		
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
		String query = "UPDATE Users SET username = ? WHERE ip = ?;";
		
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
	
	public void dropUsers() {
		try {
			this.stm = c.createStatement();
			this.rowsModified = stm.executeUpdate("DELETE FROM Users;");

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
	
	public static void main (String argv[]) {
		UserDB bd = new UserDB();
		//bd.printUsers();
		
		System.out.println("-----------");
		bd.addUser("123.123.123.123", "toto");
		bd.printUsers();
		
		System.out.println("-----------");
		
		bd.updateUser("123.123.123.123", "titi");
		
		bd.printUsers();
		System.out.println("-----------");
		
		bd.deleteUser("123.123.123.123", "titi");
		bd.printUsers();
		
		bd.dropUsers();
		
		bd.closeConnection();
		
	}

}
