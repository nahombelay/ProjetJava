package src.agent;

import java.net.Socket;

import src.database.MessagesDB;

public class InitiateConversation extends Thread{

	private final int port = 10000;
	private String ip;
	private Socket socket;
	private MessagesDB messagesDB;
	
	public InitiateConversation(String ip, MessagesDB messagesDB) {
		this.ip = ip;
		this.messagesDB = messagesDB;
		this.socket = new BindToPeer(this.ip, port).getSocket();
	}
	
	public void run() {
//		ConversationInput ci = new ConversationInput(socket, messagesDB);
//		ConversationOutput co = new ConversationOutput(socket, messagesDB);
//        ci.start();
//        co.start();
		System.out.println("Bind done !");
	}
	
	public void close() {
		ConversationInput.stop = true;
		ConversationOutput.stop = true;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	
	
//	public static void main(String[] args) {
//		new InitiateConversation("localhost", new MessagesDB()).start();
//	}

}
