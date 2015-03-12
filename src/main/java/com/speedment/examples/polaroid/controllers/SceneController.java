package com.speedment.examples.polaroid.controllers;

import com.speedment.examples.polaroid.Client;
import com.speedment.examples.polaroid.LoginController;
import static com.speedment.examples.polaroid.MainApp.PATH;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Emil Forslund
 */
public class SceneController implements Initializable {
	@FXML private HBox searchParent;
	@FXML private TextField search;
	@FXML private TilePane tilepanel;
	@FXML private VBox background;
	@FXML private Pane foreground;
	
	private final Stage root;
	private final Client client;
	
	public SceneController(Stage root) {
		this.root = root;
		this.client = new Client("http://127.0.0.1:8080");
	}

	/**
	 * Initializes the controller class.
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Platform.runLater(() -> {
			searchParent.requestFocus();
			System.out.println("Ping success: " + client.ping());
		});
	}
	
	public void showLogin() {
		showLogin("", "");
	}
	
	public void showLogin(String mail, String password) {
		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH + "/fxml/Login.fxml"));
			final LoginController controller = new LoginController(client);
			loader.setController(controller);
			final VBox box = (VBox) loader.load();
			final Popup popup = new Popup();
			
			controller.setMail(mail);
			controller.setPassword(password);
			
			controller.onLogin(m -> {
				popup.hide();
				showImages();
				foreground.setVisible(false);
				background.setEffect(null);
			});
			
			controller.onShowRegister((m, p) -> {
				popup.hide();
				showRegister(m, p);
			});
			
			popup.getContent().add(box);
			popup.show(root);
		} catch (IOException ex) {
			Logger.getLogger(SceneController.class.getName()).log(
				Level.SEVERE, "Could not find 'Login.fxml'.", ex
			);
		}
	}
	
	public void showRegister() {
		showRegister("", "");
	}
	
	public void showRegister(String mail, String password) {
		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH + "/fxml/Register.fxml"));
			final RegisterController controller = new RegisterController(client);
			loader.setController(controller);
			final VBox box = (VBox) loader.load();
			final Popup popup = new Popup();
			
			controller.setMail(mail);
			controller.setPassword(password);
			
			controller.onRegister((m, p) -> {
				popup.hide();
				foreground.setVisible(false);
				background.setEffect(null);
				controller.setMail(m);
				controller.setPassword(p);
			});
			
			controller.onCancel((m, p) -> {
				popup.hide();
				showLogin(m, p);
			});
			
			popup.getContent().add(box);
			popup.show(root);
		} catch (IOException ex) {
			Logger.getLogger(SceneController.class.getName()).log(
				Level.SEVERE, "Could not find 'Register.fxml'.", ex
			);
		}
	}
	
	public void showImages() {
		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH + "/fxml/Picture.fxml"));
			final PictureController controller = new PictureController(client);
			loader.setController(controller);
			final StackPane box = (StackPane) loader.load();
			tilepanel.getChildren().add(box);
			
		} catch (IOException ex) {
			Logger.getLogger(SceneController.class.getName()).log(
				Level.SEVERE, "Could not find 'Picture.fxml'.", ex
			);
		}
	}
	
}