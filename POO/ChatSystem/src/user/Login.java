package user;

public class Login {
	
	private String login;
	
	//peut etre ajouter @IP
	
	public Login(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
	 //verifier s'il est libre avant de l'affecter sinon ca peut poser problème 
		this.login = login;
		
		//TODO: broadcast to other users that login has changed 
		/**
		 *  To make sure that the login is unique:
		 *  	- broadcast get all userIds and logins
		 *  	- let user choose a login different from those he received
		 *  	- sends his new login and userID to all users 
		 *  	- every other user saves it to their own database and sends ACK
		 * 
		 * First login:
		 * 	- sends broadcast, receives nothing so can adds himself in database
		 */
	}
	
	/**
	 * Un thread qui tourne en permanance pour le serveur et le client
	 * De 0 à n thread selon le nombre de conversation: une convo un thread	
	 */
	
	
	
}
