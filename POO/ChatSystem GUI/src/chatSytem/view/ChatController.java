package chatSytem.view;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import chatSytem.Main;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import src.database.ActiveUsersDB;
import src.user.Login;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class ChatController {
	
	private ActiveUsersDB activeUsers;
	
	private Login login;
	
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
	private TableView<Button> tableView;
	
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
        a.setAlertType(AlertType.INFORMATION); 
        a.setTitle("About");
        a.setHeaderText("Created by Nahom Belay & Florian Leon");
        a.setContentText("Interfaces built with JavaFx and Scene Builder.\nFor more information, feel free to contact us !");
        a.show(); 
	}
	
	@FXML
	public void switchToOnline() throws Exception {
		System.out.println("Online");
		String location = ConnexionController.toogleGroupValue;
		if (location.equals("Intern")) {
			activeUsers = Main.user.getActiveUsers();
			login = Main.user.getLogin();
			activeUsers.changeStatus(login.getLogin(), "Online");
		} else if (location.equals("Extern")) {
			//TODO: Complete once servlet done
		} else {
			throw new Exception("Unable to change Status");
		}
	}
	
	@FXML
	public void switchToDND() throws Exception {
		System.out.println("Do Not Disturb");
		String location = ConnexionController.toogleGroupValue;
		if (location.equals("Intern")) {
			activeUsers = Main.user.getActiveUsers();
			login = Main.user.getLogin();
			activeUsers.changeStatus(login.getLogin(), "Do Not Disturb");
		} else if (location.equals("Extern")) {
			//TODO: Complete once servlet done
		} else {
			throw new Exception("Unable to change Status");
		}
	}
	
	@FXML
	public void switchToOffline() throws Exception {
		System.out.println("Offline");
		String location = ConnexionController.toogleGroupValue;
		if (location.equals("Intern")) {
			activeUsers = Main.user.getActiveUsers();
			login = Main.user.getLogin();
			activeUsers.changeStatus(login.getLogin(), "Offline");
		} else if (location.equals("Extern")) {
			//TODO: Complete once servlet done
		} else {
			throw new Exception("Unable to change Status");
		}
	}
	
	@FXML
	public void changeUsernameHandler() throws IOException {
		Main.showChangeUsernameLayout();
	}
	
	
	//TODO : rajouter observeur
	@FXML
	public void updateTable() throws ClassNotFoundException, SQLException {
		activeUsers = Main.user.getActiveUsers();
		ArrayList<Login> list = activeUsers.getAllUsers();
		
		for(Login l : list) {
			Button btn = new Button(); 
			btn.setMinWidth(328);
            btn.setMinHeight(50);
            btn.setText(l.getLogin());  
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override 
                public void handle(ActionEvent event) {
                    System.out.println(btn.getText()); //Or "1" as in your code
                }
            });
            tableView.getItems().add(btn);
		}
		
	}
	
	//NE marche pas encore : regarder https://stackoverflow.com/questions/12153622/how-to-close-a-javafx-application-on-window-close 
	@FXML
	public void quitHandler() {
		//TODO : il faut arreter le reste aussi je suppose pas que faire ca 
		Main.user.close();
		Platform.exit();
		//Main.closeStage();
	}
	
	//ouvrir la connenxion new Socket(ip, port);
	//Rajouter une methode close dans user ?

}
