package database;

import java.util.Hashtable;
import java.util.Set;

//This class will be replaces by UserDB
public class ActiveUsers {
	
	/**
	 * Hashtable will have as a key a String representing the ip Address of user and as a Value 
	 * a String representing its username
	 */
	private Hashtable<String, String> onlineHash;
	
	/**
	 * Constructor of the hashtable of 50 components with the key being a users unique ID and the value being the 
	 * login the user has chosen for a particular session
	 * Use of hashtable because it is synchronised compared to hashmaps
	 * 
	 */
	
	public ActiveUsers() {
		this.onlineHash = new Hashtable<String, String>(50);
	}
	
	/**
	 * Get active users
	 * @return Hashtable
	 */
	public Hashtable<String, String> getOnlineHash() {
		return onlineHash;
	}
	
	/**
	 * Add user to the current hashtable
	 * 
	 * @param key unique key (we chose the IP address)
	 * @param login the login chosen by the user for a given session
	 */
	
	public void addUser(String key, String username) {
		
		onlineHash.put(key,username);
		
	}
	/**
	 * Remove a user from a the hashtable
	 * 
	 * @param key unique key (we chose the IP address)
	 * @param login the login chosen by the user for a given session
	 */
	public void removeUser(String key, String username) {
		if (!(onlineHash.remove(key, username))) {
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
	public void updateUser(String key, String username) {
		//TODO: should send something to Login class
		onlineHash.replace(key,username);
	}
	
	/**
	 * check if hashtable contains a particular value
	 * @param value
	 * @return true if contains value, otherwise returns false
	 */
	public boolean hasValue(String value) {
		return onlineHash.containsValue(value);
	}
	
	/**
	 * check if hashtable contains a particular key
	 * @param key
	 * @return true if contains key, otherwise returns false
	 */
	public boolean hasKey(String key) {
		return onlineHash.containsKey(key);
	}
	
	@Override
	public String toString() {
		String output = "";
		Set<String> keys = onlineHash.keySet();
        for(String key: keys){
            output += key+ "-"+ onlineHash.get(key)+";";
        }
		return output;
	}
	
	/*
	 * public static void main(String[] args) { ActiveUsers au = new ActiveUsers();
	 * System.out.println(au.toString()); au.addUser("hello", "OK");
	 * System.out.println(au.toString()); au.updateUser("hello", "KO");
	 * System.out.println(au.toString()); au.removeUser("hello", "OK");
	 * au.removeUser("hello", "KO"); System.out.println(au.toString()); }
	 */

}
