package user;

public class Login {
	
	private String login;
	
	public Login(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
		
		//TODO: broadcast to other users that login has changed 
		/**
		 *  To make sure that the login is unique:
		 *  	- broadcast get all userIds and logins
		 *  	- let user choose a login different from those he received
		 *  	- sends his new login and userID to all users 
		 *  	- every other user saves it to their own database and sends ACK
		 * 
		 */
	}
	
	
}
