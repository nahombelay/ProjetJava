package chatSytem;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private Stage primaryStage;
	//private Pane connexionLayout;
	private BorderPane MainLayout;

	@Override
	public void start(Stage primaryStage) throws IOException {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Chat System");
		showMainLayout();
		showConnexionLayout();
	}


	private void showMainLayout() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("./view/MainPane.fxml"));
		MainLayout = loader.load();
		Scene scene = new Scene(MainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void showConnexionLayout() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/ConnexionPane.fxml"));
		BorderPane connexionLayout = loader.load();
		MainLayout.setCenter(connexionLayout);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
