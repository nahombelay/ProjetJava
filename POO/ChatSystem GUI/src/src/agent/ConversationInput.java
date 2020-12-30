package src.agent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import chatSytem.view.ChatController;
import src.database.MessagesDB;

public class ConversationInput extends Thread {
	
	private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();
	
	private Socket socketInput;
	private static BufferedReader inputBuffer;
	private MessagesDB messagesDB;
	private final String ipDest; 
	public static boolean stop = false;
    /**
     * 
     * @param socketInput : socket to which we'll be permanently waiting for an input
     */
	public ConversationInput(Socket socketInput, MessagesDB messagesDB) {
		this.messagesDB = messagesDB;
		this.socketInput = socketInput;
		ipDest = ((InetSocketAddress) socketInput.getRemoteSocketAddress()).toString().split("/")[1].split(":")[0];
	}
	/**
	 * At each input read, it prints it. 
	 * If null is received we close the conversation (aka the socket)
	 * A AMELIORER
	 */
	public void run()  {
		
		while(!stop) {
			try {
				inputBuffer = new BufferedReader(new InputStreamReader(socketInput.getInputStream()));
				String resp = inputBuffer.readLine();
				if (resp == null) {
					System.out.println("[ConversationInput] " + socketInput.toString() + " : End of Conversation.");
					socketInput.close();
					break;
				} 
				//System.out.println("[ConversationInput] " + socketInput.toString() + " --- " + timestamp + " : " + resp);
				//stocker le message dans la base de donnée
				messagesDB.addMessage(ipDest, false, resp);
				//send a signal to chat Controller
				notifyListeners(ChatController.class, "incomingMSG", ipDest, resp);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		System.out.println("[ConversationInput] End of Thread");
		
	}
	public Socket getSocketInput() {
		return socketInput;
	}
	
	private synchronized void notifyListeners(Object object, String property, String ip, String msg) {
        for (PropertyChangeListener name : listener) {
            name.propertyChange(new PropertyChangeEvent(this, property, ip, msg));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listener.add(newListener);
    }

}
