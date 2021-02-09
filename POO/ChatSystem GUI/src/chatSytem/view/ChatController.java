package chatSytem.view;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import chatSytem.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import src.agent.ConversationInput;
import src.agent.InitiateConversation;
import src.agent.ListenSocket;
import src.agent.MainSocket;
import src.communications.SendUDP;
import src.communications.WebSocketClient;
import src.database.ActiveUsersDB;
import src.database.MessagesDB;
import src.messages.Timestamp;
import src.user.Login;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;

public class ChatController implements PropertyChangeListener {
	
	private ActiveUsersDB activeUsers;
	
	private Login login;
	
	@FXML
	private VBox vbox;
	
	@FXML
	private ScrollPane chatPane;
	
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
	
	private Conversations convList;
	
	private MessagesDB MDB;
	
	private String activeIp = null;
	
	private ConversationInput activeCi;
	
	private ServerSocket serverSocket;
	
	private final String location = ConnexionController.toogleGroupValue;
	
	Alert a = new Alert(AlertType.NONE);

	private WebSocketClient endpoint = null; 
	
	

	@FXML
	private void initialize() throws ClassNotFoundException, SQLException, IOException {
		if (location.equals("Intern")) {
			activeUsers = Main.user.getActiveUsers();
			activeUsers.addChangeListener(this);
	        list = activeUsers.getAllUsers();
			for(Login l : list) {
				addUser(l);
			}
	        addButtonToTable();
	        convList = new Conversations();
	        this.MDB = new MessagesDB();
	        threadTCP();
	        
		} else {
			activeUsers = Main.externalUser.getActiveUsers();
			activeUsers.addChangeListener(this);
	        list = activeUsers.getAllUsers();
			for(Login l : list) {
				addUser(l);
			}
	        addButtonToTable();
	        convList = new Conversations();
	        this.MDB = new MessagesDB();
	        this.endpoint =  Main.externalUser.getEndpoint();
	        this.endpoint.addChangeListener(this);
	        
		}
        
		vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setMaxHeight(Double.MAX_VALUE);
	}
	
	/**
	 * Launch the server Thread. From then we are able to send and receive a message.
	 * @see src.agent.ListenSocket
	 * @see src.agent.MainSocket
	 */
	private void threadTCP() {
		MainSocket mainSock = new MainSocket();
		this.serverSocket = mainSock.getSocketServeur();
		ListenSocket listenSock = new ListenSocket(serverSocket, MDB);
		listenSock.addChangeListener(this);
		listenSock.start();
	}
	
	private void addUser(Login l) {
		ObservableList<Login> list = usersList;
		list.add(l);
		tableView.setItems(list);
		usersList = list;
	}
	
	/**
	 * Adds a button to the activeUsers table with the username as the displayed text and indexed with ip addresses.
	 * When clicked the button starts a new conversation with the corresponding person.
	 */
	private void addButtonToTable() {
		tableView.getColumns().clear();
		TableColumn<Login, Void> colBtn = new TableColumn<Login, Void>("Users");
		colBtn.setStyle("-fx-background-color: linear-gradient(to right, #9F8DC7, #8883B7)");
        colBtn.setMinWidth(316);
        colBtn.setMaxWidth(316);
        Callback<TableColumn<Login, Void>, TableCell<Login, Void>> cellFactory = new Callback<TableColumn<Login, Void>, TableCell<Login, Void>>() {
            @Override
            public TableCell<Login, Void> call(final TableColumn<Login, Void> param) {
                final TableCell<Login, Void> cell = new TableCell<Login, Void>() {
                    private final Button btn = new Button("Open");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                        	Login login = getTableView().getItems().get(getIndex());
                        	if (location.equals("Intern")) {
	                            startConnexion(login);
                        	} else {
                        		startConnexionExternal(login);
                        	}
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
                            btn.setPrefWidth(305);
                            btn.setMinWidth(305);
                            btn.setMaxWidth(305);
                            btn.setWrapText(true);
                            Font font = Font.font("Arial", FontWeight.BOLD, 15);
                            btn.setFont(font);
                            String status = activeUsers.getStatus(login.getIp());
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
	private void conversationOpen(boolean isOpen) {
		conversationWith.setVisible(isOpen);
		endConvoButton.setVisible(isOpen);
		chooseUser.setVisible(!isOpen);
		buttonBar.setVisible(isOpen);
	}
	
	private void updateConvoWithLabel(String user) {
		conversationWith.setText("Conversation with " + user);
		conversationWith.setVisible(true);
	}
	
	private void updateDateLabel(String date) {
		dateLabel.setText("Date : " + date);
		dateLabel.setVisible(true);
	}
	
	@FXML
	private void aboutSection() {
        a.setAlertType(AlertType.INFORMATION); 
        a.setTitle("About");
        a.setHeaderText("Created by Nahom Belay & Florian Leon");
        a.setContentText("Interfaces built with JavaFx and Scene Builder.\nFor more information, feel free to contact us !");
        a.show(); 
	}
	
	@FXML
	private void switchToOnline() throws Exception {
		//TODO: have to check that the user isn't already online
		System.out.println("Online");
		String location = ConnexionController.toogleGroupValue;
		if (location.equals("Intern")) {
			activeUsers = Main.user.getActiveUsers();
			login = Main.user.getLogin();
			String currentStatus = activeUsers.getStatus(login.getIp());
			if (currentStatus.equals("Offline")) {
				//add user from active users list of everyone
				SendUDP.send("[1BD]:" + login.toString(), InetAddress.getByName("255.255.255.255"), 20000, true);
			} else if (currentStatus.equals("Do Not Disturb")) {
				//Send a broadcast to update status to other users
				SendUDP.send("[DND]:" + login.toString(), InetAddress.getByName("255.255.255.255"), 20000, true);
			} 
			//Pas sur que cette ligne soit necessaire maintenant…
			activeUsers.changeStatus(login.getLogin(), "Online");
		} else if (location.equals("Extern")) {
			String sourceIP = Main.externalUser.getLogin().getIp();
			String username = Main.externalUser.getLogin().getLogin();
			if (endpoint != null) {
				String message = "[UserUpdate]:" + sourceIP + ":" + username + ":Online:ExternalUsers:";
				try {
					this.endpoint.sendMessage(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			throw new Exception("Unable to change Status");
		}
	}
	
	@FXML
	private void switchToDND() throws Exception {
		System.out.println("Do Not Disturb");
		String location = ConnexionController.toogleGroupValue;
		if (location.equals("Intern")) {
			activeUsers = Main.user.getActiveUsers();
			login = Main.user.getLogin();
			activeUsers.changeStatus(login.getLogin(), "Do Not Disturb");
		} else if (location.equals("Extern")) {
			String sourceIP = Main.externalUser.getLogin().getIp();
			String username = Main.externalUser.getLogin().getLogin();
			if (endpoint != null) {
				String message = "[UserUpdate]:" + sourceIP + ":" + username + ":Do Not Disturb:ExternalUsers:";
				try {
					this.endpoint.sendMessage(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			throw new Exception("Unable to change Status");
		}
	}
	
	@FXML
	private void switchToOffline() throws Exception {
		System.out.println("Offline");
		String location = ConnexionController.toogleGroupValue;
		if (location.equals("Intern")) {
			activeUsers = Main.user.getActiveUsers();
			login = Main.user.getLogin();
			activeUsers.changeStatus(login.getLogin(), "Offline");
			//Send a broadcast to notify other users that you've got offline
			SendUDP.send("[OFF]:" + login.toString(), InetAddress.getByName("255.255.255.255"), 20000, true);
		} else if (location.equals("Extern")) {
			String sourceIP = Main.externalUser.getLogin().getIp();
			String username = Main.externalUser.getLogin().getLogin();
			if (endpoint != null) {
				String message = "[UserUpdate]:" + sourceIP + ":" + username + ":Offline:ExternalUsers:";
				try {
					this.endpoint.sendMessage(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			throw new Exception("Unable to change Status");
		}
	}
	
	@FXML
	private void changeUsernameHandler() throws IOException {
		Main.showChangeUsernameLayout();
	}

	//NE marche pas encore : regarder https://stackoverflow.com/questions/12153622/how-to-close-a-javafx-application-on-window-close 
	@FXML
	private void quitHandler() {
		//TODO : il faut arreter le reste aussi je suppose pas que faire ca
		if (location.equals("Intern")) {
			Login login = Main.user.getLogin();
			//remove user from active users list of everyone
			try {
				SendUDP.send("[RAU]:" + login.toString(), InetAddress.getByName("255.255.255.255"), 20000, true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Main.user.close();
		} else {
			//Main.externalUser.close();
		}
		Platform.exit();
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
			try {
				activeUsers.changeStatus(event.getOldValue().toString(), event.getNewValue().toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			updateTable();
			tableView.refresh();
		} else if (event.getPropertyName().equals("incomingMSG")) {
			incomingMSG(event.getOldValue().toString(), event.getNewValue().toString());
		} else if (event.getPropertyName().equals("accept")) {
			startConv(event.getOldValue().toString(), (Socket) event.getNewValue());
		} else {
			System.out.println("Wrong event");
		}
		
	}
	/**
	 * Handler when we receive an accept from the listenUsers class. 
	 * It's used when we didn't start the conversation. 
	 * It launches a new Thread. 
	 * @param ip
	 * @param sock
	 * @see src.agent.ListenUsers
	 * @see src.agent.ConversationInput
	 */
	private void startConv(String ip, Socket sock) {
		convList.addConv(ip, sock);
		ConversationInput ci = new ConversationInput(sock, MDB);
		activeCi = ci;
		activeCi.addChangeListener(this);
		activeCi.start();
	}
	/**
	 * Handler for an incoming message : we first check if the message is for a conversation that is open
	 * if so then we display the message else we have nothing to do because the message is
	 * already stored in the DB. We need to invoke the display method with Platform.runLater 
	 * to avoid conflict with all the threads
	 * @param ip : the sender of the message
	 * @param msg : the incoming message
	 */
	private void incomingMSG(String ip, String msg) {
		//look if the conversation is active
		boolean test = activeIp.equals(ip);
		if (test) {
			//then if the conversation is active we display the message
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
			        // Update UI
			    	display(msg, false, Timestamp.formatDateTimeFull());
			    }
			});
		}
	}
	
	/**
	 * Delete a new user from the activeUsers table
	 * @param ip to Delete
	 * @param username associated with the ip
	 */
	private void deleteUserHandler(String ip, String username) {
		ObservableList<Login> list = tableView.getItems();
		int size = list.size();
		if(size == 0) {
			System.out.println("Empty List");
		} else {
			int index = 0;
			for(int i = 0; i < size; i++) {
				if (list.get(i).getIp().equals(ip)) {
					index = i;
					break;
				}
			}
			list.remove(index);
		}
		activeUsers.deleteUser(ip, username);
		tableView.setItems(list);
		addButtonToTable();
	}

	/**
	 * Method used to change the username of any user
	 * @param oldUsername to be replaced
	 * @param username to change
	 */
	private void updateUserHandler(String oldUsername, String username) {
		String ip = activeUsers.getCurrentIp(username);
		Login newLogin = new Login(username, ip);
		ObservableList<Login> list = tableView.getItems();
		int size = list.size();
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

	/**
	 * Add a new user to the activeUsers table
	 * @param ip to add
	 * @param username associated with the ip
	 */
	private void newUserHandler(String ip, String username) {
		if (location.equals("Intern")) {
			activeUsers = Main.user.getActiveUsers(); 
		} else {
			activeUsers = Main.externalUser.getActiveUsers(); 
		} 
		  try { 
			  list = activeUsers.getAllUsers(); 
			  addUser(list.get(list.size()-1));
			  addButtonToTable(); 
		  } catch (ClassNotFoundException | SQLException e) { 
			  // TODO Auto-generated catch block 
			  e.printStackTrace(); 
		}
	}
	
	/**
	 * It is supposed to update the table
	 * Not tested yet 
	 */
	private void updateTable() {
		if (location.equals("Intern")) {
			activeUsers = Main.user.getActiveUsers(); 
		} else {
			activeUsers = Main.externalUser.getActiveUsers(); 
		} 
		addButtonToTable(); 
	}
	
	/**
	 * Starts a conversation with a given person
	 * and handles the interface to display the conversation
	 * @param login : person to start the conversation with
	 */
	private void startConnexion(Login login) {
		String ip = login.getIp();
		this.activeIp = ip;
		conversationOpen(true);
		updateConvoWithLabel(login.getLogin());
		updateDateLabel(Timestamp.formatDateTime());
		Socket sock = null;
		if (!convList.hasKey(ip)) {
			System.out.println("No key found");
			InitiateConversation conv = new InitiateConversation(ip, MDB);
			conv.start();
			sock = conv.getSocket();
			convList.addConv(ip, sock);
		} else {
			sock = convList.getSocket(ip);
			
		}
		ConversationInput ci = new ConversationInput(sock, MDB);
		activeCi = ci;
		activeCi.addChangeListener(this);
		activeCi.start();
		vbox.getChildren().clear();
		displayHistory(ip);
	}
	
	private void startConnexionExternal(Login login) {
		String ip = login.getIp();
		this.activeIp = ip;
		conversationOpen(true);
		updateConvoWithLabel(login.getLogin());
		updateDateLabel(Timestamp.formatDateTime());
		vbox.getChildren().clear();
		displayHistory(ip);
	}

	/**
	 * Close the active conversation by calling the function conversationOpen(Boolean) and remove the conversation from the conversation list
	 * @throws IOException if we don't find the socket
	 */
	@FXML
	private void closeConv() throws IOException {
		String ip = activeIp;
		if (ip == null) {
			System.out.println("Select a user to get his ip");
		} else {
			//Socket sock = convList.getSocket(ip);
			//convList.removeConv(ip, sock);
			textArea.setText(null);
			sendMessage();
			Socket sock = convList.getSocket(ip);
			deleteUserHandler(ip, activeUsers.getCurrentUsername(ip));
			convList.removeConv(ip, sock);
			textArea.setText("");
		}
		conversationOpen(false);
	}
	
	/**
	 * • Take the text in the text area 
	 * • Open an OutputStram to send the message
	 * • Add the message to the local database
	 * • Send the message
	 * • Display the message
	 * @exception IOException if we are unable to get the right socket
	 */
	@FXML
	private void sendMessage() {
		if (location.equals("Intern")) {
			String ip = activeIp;
			Socket sock = convList.getSocket(ip);
			OutputStream out = null;
			try {
				out = sock.getOutputStream();
				if (ip == null) {
					System.out.println("Select a user to get his ip");
				} else {
					String text = textArea.getText();
					if (text == null) {
						PrintWriter writer = new PrintWriter(out);
						writer.write(text + "\n");
						writer.flush();
						textArea.setText("");
					} else if (text.equals("")) {
						System.out.println("Empty Message");
					} else {
						MDB.addMessage(ip, true, text);
						//send message
						PrintWriter writer = new PrintWriter(out);
						writer.write(text + "\n");
						writer.flush();
						//display the message
						String timestamp = Timestamp.formatDateTimeFull();
						display(text, true, timestamp);
						textArea.setText("");
					}	
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			String sourceIP = Main.externalUser.getLogin().getIp();
			String destIP = this.activeIp;
			
			String message = null;
			
			if (destIP == null) {
				System.out.println("Select a user to get his ip");
			} else {
				String text = textArea.getText();
				if (text.equals("")) {
					System.out.println("Empty Message");
				} else {
					MDB.addMessage(destIP, true, text);
					//send message
					//use endpoint to send message 
					if (endpoint != null) {
						message = "[Forward]:" + sourceIP + ":" + destIP + ":ExternalUsers:" + text;
						try {
							this.endpoint.sendMessage(message);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					//display the message
					String timestamp = Timestamp.formatDateTimeFull();
					display(text, true, timestamp);
					textArea.setText("");
				}	
			}
			
		}
		
	}
	
	/**
	 * Displays one message on the chatPane on one side or an other depending on the message is from the person who sends or receives it
	 * @param msg : message to display
	 * @param isSender : if true the message in place at the right side of the panel, left side otherwise
	 * @param timestamp : when the message was send or received
	 */
	private void display(String msg, boolean isSender, String timestamp) {
		if (msg.equals("null")) {
			conversationOpen(false);
		} else {
			Label label = new Label(); 
			Label tLabel = new Label();
			tLabel.setFont(new Font("Arial", 7));
			tLabel.setScaleX(0.7);
			tLabel.setScaleY(0.7);
			tLabel.setText(timestamp);
			tLabel.setVisible(false);
			label.setWrapText(true);
			label.setPadding(new Insets(5,10,5,10));
	        label.setFont(new Font("Arial", 14));
	        label.setText(msg);
	        label.setOnMouseEntered(new EventHandler<MouseEvent>() {
			    @Override public void handle(MouseEvent e) {
			        tLabel.setVisible(true);
			        label.setScaleX(1.1);
			        label.setScaleY(1.1);
			    }
			});
			label.setOnMouseExited(new EventHandler<MouseEvent>() {
			    @Override public void handle(MouseEvent e) {
			    	tLabel.setVisible(false);
			    	label.setScaleX(1);
			    	label.setScaleY(1);
			    }
			});
			HBox hBox = new HBox();
			hBox.setPadding(new Insets(0,5,0,0));
			if (isSender) {
				label.setStyle("-fx-background-color: #0078FF; -fx-background-radius: 10px 10px 0px 10px;");
				label.setTextFill(Color.web("#ffffff"));
		        hBox.getChildren().add(tLabel);
		        hBox.getChildren().add(label);
		        hBox.setAlignment(Pos.BASELINE_RIGHT);
			} else {
				label.setStyle("-fx-background-color: #dfe1ee; -fx-background-radius: 10px 10px 10px 0px;");
				hBox.getChildren().add(label);
				hBox.getChildren().add(tLabel);
			    hBox.setAlignment(Pos.BASELINE_LEFT);
			}
			vbox.getChildren().add(hBox);
			chatPane.setVvalue(chatPane.getVvalue()+ label.getHeight() + 10);
		}
	}
	
	/**
	 * Fetch messages history from database given a destination ip
	 * @param ip : ip of the open conversation
	 */
	private void displayHistory(String ip) {
		ArrayList<ArrayList<String>> array = MDB.getMessages(ip);
		if (array != null) {
			ArrayList<String> textArray = array.get(0);
			ArrayList<String> timeArray = array.get(1);
			ArrayList<String> posArray = array.get(2);
			int size = textArray.size();
			boolean isSender;
			for (int i = 0 ; i < size ; i++) {
				if (posArray.get(i).equals("true")) {
					isSender = true;
				} else {
					isSender = false;
				}
				display(textArray.get(i), isSender, timeArray.get(i));
			}
		}
	}
	

}
