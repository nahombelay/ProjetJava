package database;

import user.Login;
import java.util.Hashtable;


public class ActiveUsers {
	
	
	private Hashtable<String, Login> onlineHash;
	
	/**
	 * Constructor of the hashtable of 50 components with the key being a users unique ID and the value being the 
	 * login the user has chosen for a particular session
	 * Use of hashtable because it is synchronised compared to hashmaps
	 * 
	 */
	
	public ActiveUsers() {
		this.onlineHash = new Hashtable<String, Login>(50);
	}
	
	/**
	 * Get active users
	 * @return Hashtable
	 */
	public Hashtable<String, Login> getOnlineHash() {
		return onlineHash;
	}
	
	/**
	 * Add user to the current hashtable
	 * 
	 * @param key unique key (we chose the IP address)
	 * @param login the login chosen by the user for a given session
	 */
	
	public void addUser(String key, Login login) {
		try {
			if (onlineHash.put(key,login) == null) {
				System.out.println("Failed to add user");
				//TODO: add proper exception and remove print statement
			}
		} catch (NullPointerException E) {
			//TODO: bien traiter cette exception
			E.printStackTrace();
		}
		
	}
	/**
	 * Remove a user from a the hashtable
	 * 
	 * @param key unique key (we chose the IP address)
	 * @param login the login chosen by the user for a given session
	 */
	public void removeUser(String key, Login login) {
		if (!(onlineHash.remove(key, login))) {
			System.out.println("Failed to remove user");
			//TODO: add proper exception and remove print statement
		}
	}
	
	/**
	 * Updates hashtable and changes Login 
	 * 
	 * @param key unique key (we chose the IP address)
	 * @param login the login chosen by the user for a given session
	 */
	public void updateUser(String key, Login login) {
		//TODO: should send something to Login class
		onlineHash.replace(key,login);
	}

}
