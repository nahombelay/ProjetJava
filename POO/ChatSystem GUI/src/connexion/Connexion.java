package connexion;

import java.io.IOException;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Connexion extends Application {
	
	private Stage primaryStage;
	private Pane connexionLayout;

	@Override
	public void start(Stage primaryStage) throws IOException {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Connexion Screen");
		showConnexionLayout();
	}

	private void showConnexionLayout() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Connexion.class.getResource("view/ConnexionPane.fxml"));
		connexionLayout = loader.load();
		Scene scene = new Scene(connexionLayout);
		scene.getStylesheets().addAll(this.getClass().getResource("./view/connexion.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
