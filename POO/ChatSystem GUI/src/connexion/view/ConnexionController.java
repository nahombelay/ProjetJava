package connexion.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class ConnexionController {
	
	ObservableList<String> choices = FXCollections.observableArrayList("Online","Do not disturb","Offline");
	
	@FXML
	private ChoiceBox<String> choiceBox;
	
	@FXML
	private void initialize() {
		choiceBox.setValue("Status");
		choiceBox.setItems(choices);
	}
	

}
