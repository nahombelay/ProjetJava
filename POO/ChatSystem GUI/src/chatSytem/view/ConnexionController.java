package chatSytem.view;

import java.io.IOException;

import chatSytem.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class ConnexionController {
	
	ObservableList<String> choices = FXCollections.observableArrayList("Online","Do not disturb","Offline");
	
	@FXML
	private ChoiceBox<String> choiceBox;
	
	@FXML
	private TextField usernameField;
	
	@FXML
	private ToggleGroup Location;
	
	
	
	@FXML
	private void initialize() {
		choiceBox.setValue("Status");
		choiceBox.setItems(choices);
	}
	
	@FXML
	private void connection() throws IOException {
	
		//Get the selected radio button
		RadioButton selectedRadioButton = (RadioButton) Location.getSelectedToggle();
		String toogleGroupValue = selectedRadioButton.getText();
	
		//TODO: Launch the right method whether the user is intern or not
		if (toogleGroupValue == "Intern") {
			//On lance un nouvel user ? 
			System.out.println(toogleGroupValue);
		} else {
			//On lance la version avec servlet
			System.out.println(toogleGroupValue);
		}
		//Get username
		String username = usernameField.getText();
		//TODO: Check if it's unique --> we shoudn't continue if the username is not unique 
		System.out.println(username);
		
		//Get the status
		String status = choiceBox.getSelectionModel().getSelectedItem();
		//Send Status to the servlet
		System.out.println(status);
		//Last part before displaying the chat screen
		//Main.showMainLayout();
		//TODO: Display the chat screen (once created…)
		
	}

}
