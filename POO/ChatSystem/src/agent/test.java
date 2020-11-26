package agent;

import java.io.IOException;

public class test {

	public static void main(String[] args) throws IOException {
		
		Server S = new Server(6666);
		Client C = new Client("127.0.0.1", 6666);
		

	}

}
