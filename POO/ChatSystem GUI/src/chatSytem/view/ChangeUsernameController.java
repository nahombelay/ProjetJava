package chatSytem.view;

import java.io.IOException;
import java.net.UnknownHostException;

import ChatSystem.gui.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import src.database.ActiveUsersDB;

public class ChangeUsernameController {
	
	@FXML
	private TextField textField;
	
	@FXML 
	private Button button;
	
	@FXML
	private Label userTaken;
	
	
	public void buttonHandler() throws UnknownHostException, IOException {
		String newUsername = textField.getText();
		boolean usernameChanged = false;
		if (ConnexionController.toogleGroupValue.equals("Intern")) {
			usernameChanged = Main.user.changeUsername(newUsername);
		} else {
			usernameChanged = Main.externalUser.changeUsername(newUsername, "Online");
		}
		
		if (usernameChanged) {
			System.out.println("[Change Username Controller] Username changed to : " + newUsername);
			//Close the UI
			Main.hideChangeUsernameLayout();
		} else {
			userTaken.setVisible(true);
			textField.setText("");
		}
	}
	
}
