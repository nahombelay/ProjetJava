package src.display;

import java.util.Scanner; 

public class DisplayMessage {

	Scanner inText;
	
	public DisplayMessage() {
		// TODO Auto-generated constructor stub
	}
	
	public String readInput() { 
		inText = new Scanner(System.in);
		String S = inText.nextLine();
		System.out.println("---" + S);
		return S;
	}

}
