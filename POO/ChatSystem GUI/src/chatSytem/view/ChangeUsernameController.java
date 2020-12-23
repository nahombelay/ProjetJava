package chatSytem.view;

import chatSytem.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ChangeUsernameController {
	
	@FXML
	private TextField textField;
	
	@FXML 
	private Button button;
	
	public void buttonHandler() {
		String newUsername = textField.getText();
		//trouver le moyen d'accerder à l'instance de user pour appeler les méthodes pour changer l'username
		System.out.println(newUsername);
		//Check the username is good 
		
		//Close the UI
		Main.hideChangeUsernameLayout();
	}
	
}
