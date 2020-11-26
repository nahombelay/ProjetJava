package user;

import agent.*;

public class User {

	public User() {
		System.out.println("[User]: Establishing connectino i.e opening listening server");
		//Establish connection 
		ThreadServer t = new ThreadServer();
		
	}
	
	public static void main(String[] args) {
		User u = new User();
	}
}
