package ChatSystem.gui;

import java.net.Socket;
import java.util.Hashtable;
import java.util.Set;

//This class will be replaces by UserDB
public class Conversations {
	
	/**
	 * Hashtable will have as a key a String representing the ip Address of user and the soscket associated
	 */
	private Hashtable<String, Socket> conversationList;
	

	
	public Conversations() {
		this.conversationList = new Hashtable<String, Socket>(50);
	}
	
	public Hashtable<String, Socket> getOnlineHash() {
		return conversationList;
	}
	
	public void addConv(String ip, Socket sock) {
		
		this.conversationList.put(ip,sock);
		
	}

	public void removeConv(String ip, Socket sock) {
		if (!(conversationList.remove(ip, sock))) {
			System.out.println("Failed to remove conv");
			//TODO: add proper exception and remove print statement
		}
	}

	public boolean hasSocket(Socket value) {
		return conversationList.containsValue(value);
	}
	
	public boolean hasKey(String ip) {
		return conversationList.containsKey(ip);
	}
	
	public Socket getSocket(String ip) {
		return conversationList.get(ip);
	}
	
	@Override
	public String toString() {
		String output = "";
		Set<String> keys = conversationList.keySet();
        for(String key: keys){
            output += key+ "-"+ conversationList.get(key)+";";
        }
		return output;
	}
	

}
