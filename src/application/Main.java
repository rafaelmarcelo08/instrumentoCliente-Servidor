package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

/**
 * Classe que define a inicialização do programa
 * 
 * @author Rafael Marcelo
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = new AnchorPane();
			root = FXMLLoader.load(getClass().getResource("/view/viewInstrumento.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
