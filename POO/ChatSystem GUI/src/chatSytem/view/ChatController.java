package chatSytem.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import chatSytem.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import src.database.ActiveUsersDB;
import src.user.Login;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;

public class ChatController implements PropertyChangeListener {
	
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
	private TableView<Login> tableView;
	
	//private TableColumn<Login, String> tbc;
	
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
	
	private ObservableList<Login> usersList = FXCollections.observableArrayList();
	
	private ArrayList<Login> list;
	
	Alert a = new Alert(AlertType.NONE); 
	
	@FXML
	private void initialize() throws ClassNotFoundException, SQLException {
        activeUsers = Main.user.getActiveUsers();
        activeUsers.addChangeListener(this);
        list = activeUsers.getAllUsers();
		for(Login l : list) {
			addUser(l);
		}
        addButtonToTable();
	}
	
	
	private void addUser(Login l) {
		ObservableList<Login> list = usersList;
		list.add(l);
		tableView.setItems(list);
		usersList = list;
	}
	
	private void addButtonToTable() {
		tableView.getColumns().clear();
		
		TableColumn<Login, Void> colBtn = new TableColumn<Login, Void>("Users");
        colBtn.setMinWidth(318);
        colBtn.setMaxWidth(318);
        
        Callback<TableColumn<Login, Void>, TableCell<Login, Void>> cellFactory = new Callback<TableColumn<Login, Void>, TableCell<Login, Void>>() {
            @Override
            public TableCell<Login, Void> call(final TableColumn<Login, Void> param) {
                final TableCell<Login, Void> cell = new TableCell<Login, Void>() {
                	
                    private final Button btn = new Button("Open");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                        	//TODO: change event :: fonction connect()
                        	Login login = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + login);
                        });
                        
                    }

					@Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                            Login login = getTableView().getItems().get(getIndex());
                            btn.setText(login.getLogin());
                            btn.setPrefWidth(318);
                            btn.setMinWidth(318);
                            btn.setMaxWidth(318);
                            btn.setWrapText(true);
                            Font font = Font.font("Arial", FontWeight.BOLD, 15);
                            btn.setFont(font);
                            String status = activeUsers.getStatus(login.getIp());
                            System.out.println(status);
                            if (status.equals("Online")) {
                            	//vert
                            	btn.setStyle("-fx-background-color: #00ff00");
                            } else if (status.equals("Do Not Disturb")) {
                            	//Violet
                            	btn.setStyle("-fx-background-color: #7f00ff");
                            } else if (status.equals("Offline")) {
                            	//rouge
                            	btn.setStyle("-fx-background-color: #ff0000");
                            } else {
                            	//white
                            	btn.setStyle("-fx-background-color: #ffffff");
                            }
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        tableView.getColumns().add(colBtn);

    }
	
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

	//NE marche pas encore : regarder https://stackoverflow.com/questions/12153622/how-to-close-a-javafx-application-on-window-close 
	@FXML
	public void quitHandler() {
		//TODO : il faut arreter le reste aussi je suppose pas que faire ca 
		Main.user.close();
		Platform.exit();
		//Main.closeStage();
	}


	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals("newUser")) {
			newUserHandler(event.getOldValue().toString(), event.getNewValue().toString());
		} else if (event.getPropertyName().equals("updateUser")) {
			updateUserHandler(event.getOldValue().toString(), event.getNewValue().toString());
		} else if (event.getPropertyName().equals("deleteUser")) {
			deleteUserHandler(event.getOldValue().toString(), event.getNewValue().toString());
		}  else if (event.getPropertyName().equals("changeStatus")) {
			tableView.refresh();
		} else {
			System.out.println("Wrong event");
		}
		
	}


	private void deleteUserHandler(String ip, String username) {
		//delUser(new Login(username, ip));
		activeUsers = Main.user.getActiveUsers();
		try {
			list = activeUsers.getAllUsers();
			addButtonToTable();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private void updateUserHandler(String oldUsername, String username) {

		String ip = activeUsers.getCurrentIp(username);
		Login newLogin = new Login(username, ip);
		ObservableList<Login> list = tableView.getItems();
		int size = list.size();
		System.out.println(size);
		if(size == 0) {
			list.add(newLogin);
		} else if (size == 1) {
			list.add(newLogin);
			list.remove(0);
		} else {
			int index = 0;
			for(int i = 0; i < size; i++) {
				if (list.get(i).getIp().equals(ip)) {
					//on recupere le premier indice à supprimer
					//si on met le list.remove on a un debordement de tableau
					index = i;
					break;
				}
			}
			list.remove(index);
			list.add(newLogin);
		}
	
		tableView.setItems(list);
		addButtonToTable();
	}


	private void newUserHandler(String ip, String username) {
		  activeUsers = Main.user.getActiveUsers(); 
		  try { 
			  list = activeUsers.getAllUsers(); 
			  addUser(list.get(list.size()-1));
			  addButtonToTable(); 
		  } catch (ClassNotFoundException | SQLException e) { 
			  // TODO Auto-generated catch block 
			  e.printStackTrace(); 
		}
		 
		
	}
	

}
