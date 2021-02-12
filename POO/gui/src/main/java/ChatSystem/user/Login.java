package ChatSystem.user;


public class Login {
	
	private String login;
	private String ip;
	
	/** 
	 * 
	 * @param login String representing the username 
	 * @param ip String representing the ip Address
	 */
	public Login(String login, String ip) {
		this.login = login;
		this.ip = ip;
	}

	/**
	 * Returns the username of user
	 * @return String: username 
	 */
	public String getLogin() {
		return login;
	}
	
	public String getIp() {
		return ip;
	}

	/**
	 * Change user's username
	 * @param login
	 */
	public void setLogin(String login) {
		this.login = login;
		
	}
	
	/**
	 * Un thread qui tourne en permanance pour le serveur et le client
	 * De 0 à n thread selon le nombre de conversation: une convo un thread	
	 */
	
	public String toString() {
		return this.login + ":" + this.ip;
	}
	
	
}
