package chatSytem.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class ChatController {
	
	//To set as invisble when it starts end visble when we are in a conversation
	@FXML
	private Button endConvoButton;
	
	@FXML 
	private Button sendButton;
	
	@FXML
	private TextArea textArea;
	
	@FXML 
	private Label chooseUser;
	
	@FXML 
	private Label conversationWith;
	
	@FXML 
	private Label dateLabel;
	
	@FXML
	private TableView tableView;
	
	@FXML
	private TableColumn tableViewColumn;
	
	@FXML 
	private ButtonBar buttonBar;
	
	@FXML 
	private MenuItem quitItem;
	
	@FXML
	private MenuItem changeUsernameItem;
	
	@FXML
	private MenuItem onlineItem;
	
	@FXML
	private MenuItem doNotDisturbItem;
	
	@FXML
	private MenuItem offlineItem;
	
	@FXML
	private MenuItem aboutItem;
	
	@FXML
	private Menu status;
	
	Alert a = new Alert(AlertType.NONE); 
	
	
	//Method to display and hide chooseUser, endConvoButton and button bar
	public void conversationOpen(boolean isOpen) {
		conversationWith.setVisible(isOpen);
		endConvoButton.setVisible(isOpen);
		chooseUser.setVisible(!isOpen);
		buttonBar.setVisible(isOpen);
	}
	
	public void updateConvoWithLabel(String user) {
		conversationWith.setText("Conversation with " + user);
		conversationWith.setVisible(true);
	}
	
	public void updateDateLabel(String date) {
		dateLabel.setText("Conversation with " + date);
		dateLabel.setVisible(true);
	}
	@FXML
	public void aboutSection() {
		 // set alert type 
        a.setAlertType(AlertType.INFORMATION); 
        a.setTitle("About");
        a.setHeaderText("Created by Nahom Belay & Florian Leon");
        a.setContentText("Interfaces built with JavaFx and Scene Builder.\nFor more information, feel free to contact us !");
        // show the dialog 
        a.show(); 
	}
	
	@FXML
	public void switchToOnline() {
		System.out.println("Online");
		//changer le statut a online
	}
	
	@FXML
	public void switchToDND() {
		System.out.println("Do Not Disturb");
		//changer le statut a online
	}
	
	@FXML
	public void switchToOffline() {
		System.out.println("Offline");
		//changer le statut a online
	}
	

}
