package chatSytem.view;

import chatSytem.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import src.database.ActiveUsersDB;
import src.user.User;

public class ConnexionController {
	
	ObservableList<String> choices = FXCollections.observableArrayList("Online","Do Not Disturb","Offline");
	
	@FXML
	private ChoiceBox<String> choiceBox;
	
	@FXML
	private TextField usernameField;
	
	@FXML
	private ToggleGroup Location;
	
	@FXML
	private Label userTaken;
	
	private ActiveUsersDB activeUsers;
	
	protected static String toogleGroupValue;
	
	
	
	@FXML
	private void initialize() {
		choiceBox.setValue("Status");
		choiceBox.setItems(choices);
	}
	
	@FXML
	private void connection() throws Exception {
		
		userTaken.setVisible(false);
		
		//Get username
		String username = usernameField.getText();
	
		//Get the selected radio button
		RadioButton selectedRadioButton = (RadioButton) Location.getSelectedToggle();
		toogleGroupValue = selectedRadioButton.getText();
	
		if (toogleGroupValue.equals("Intern")) {
			//On lance un nouvel user ? 
			System.out.println(toogleGroupValue);
			Main.user = new User();
			activeUsers = Main.user.getActiveUsers();
			//Check if it's unique --> we shoudn't continue if the username is not unique 
			boolean usernameChanged = Main.user.changeUsername(username);
			if (usernameChanged) {
				//Get the status
				String status = choiceBox.getSelectionModel().getSelectedItem();
				activeUsers.changeStatus(username, status);
				//Last part before displaying the chat screen
				Main.showChatLayout();
				
			} else {
				userTaken.setVisible(true);
				usernameField.setText("");
				
			}
			
		} else {
			//TODO: On lance la version avec servlet
			System.out.println(toogleGroupValue);
		}

	}


}
