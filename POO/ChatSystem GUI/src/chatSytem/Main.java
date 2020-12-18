package chatSytem;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private Stage primaryStage;
	private static BorderPane MainLayout;
	private static BorderPane connexionLayout;
	private static BorderPane chatLayout;

	@Override
	public void start(Stage primaryStage) throws IOException {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Chat System");
		showMainLayout();
		showConnexionLayout();
	}


	public void showMainLayout() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("./view/MainPane.fxml"));
		MainLayout = loader.load();
		Scene scene = new Scene(MainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void showConnexionLayout() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("./view/ConnexionPane.fxml"));
		connexionLayout = loader.load();
		MainLayout.setCenter(connexionLayout);
		connexionLayout.setPadding(new Insets(250, 475, 250, 475));
	}
	
	public static void showChatLayout() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("./view/ChatPane.fxml"));
		chatLayout = loader.load();
		connexionLayout.setVisible(false);
		MainLayout.setCenter(chatLayout);
	}


	public static void main(String[] args) {
		launch(args);
	}
}
