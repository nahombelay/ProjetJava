package src.agent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import chatSytem.view.ChatController;
import src.database.MessagesDB;

public class ListenSocket extends Thread {
	
	private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();
	
	private ServerSocket serverSocket;
	private Socket socketConversation;
	private MessagesDB messagesDB;
	public static boolean stop = false;

    /**
     * 
     * @param socket : take Server socket from mainSocket
     */
	public ListenSocket(ServerSocket socket, MessagesDB messagesDB) {
		this.serverSocket = socket;
		this.messagesDB = messagesDB;
	}
	
	/**
	 * At each new bind creates a thread for sending and receiving messages
	 */
	public void run() {
		System.out.println("[ListenSocket] Attente bind...");
		while (!stop) {
			try {
				socketConversation = serverSocket.accept();
				notifyListeners(ChatController.class, "accept", socketConversation.getInetAddress().toString().substring(1), socketConversation);
				System.out.println("[ListenSocket] New accept, socket " + socketConversation.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      }
		System.out.println("[ListenSocket] End of Thread");
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	private synchronized void notifyListeners(Object object, String property, String ip, Socket socket) {
        for (PropertyChangeListener name : listener) {
            name.propertyChange(new PropertyChangeEvent(this, property, ip, socket));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listener.add(newListener);
    }

	public MessagesDB getMessagesDB() {
		return messagesDB;
	}

}
