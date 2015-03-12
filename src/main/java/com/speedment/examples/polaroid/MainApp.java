package com.speedment.examples.polaroid;

import com.speedment.examples.polaroid.controllers.SceneController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class MainApp extends Application {
	
	public final static String PATH = "/com/speedment/examples/polaroid";

    @Override
    public void start(Stage stage) throws Exception {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH + "/fxml/Scene.fxml"));
        final SceneController control = new SceneController(stage);
		loader.setController(control);
		
		final StackPane root = (StackPane) loader.load();
        final Scene scene = new Scene(root);
		
        scene.getStylesheets().add(PATH + "/styles/Styles.css");
        
        stage.setTitle("Polaroid Social Network");
        stage.setMaximized(true);
		stage.setScene(scene);
        stage.show();

		control.showLogin();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}