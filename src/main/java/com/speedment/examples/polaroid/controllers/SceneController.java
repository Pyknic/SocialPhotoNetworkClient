package com.speedment.examples.polaroid.controllers;

import com.speedment.examples.polaroid.Client;
import com.speedment.examples.polaroid.JSONImage;
import static com.speedment.examples.polaroid.MainApp.PATH;
import static com.speedment.examples.polaroid.util.DropHelper.handleDrop;
import static com.speedment.examples.polaroid.util.DropHelper.handleOver;
import com.speedment.examples.polaroid.util.FadeAnimation;
import com.speedment.examples.polaroid.util.LayoutUtil;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Emil Forslund
 */
public class SceneController implements Initializable {
	
	@FXML private Pane container;
	@FXML private HBox searchParent;
	@FXML private Button buttonProfile;
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
		SearchController.showIn(container.getChildren(), search, client);
		buttonProfile.setText("");
		
		background.prefWidthProperty().bind(container.widthProperty());
		background.prefHeightProperty().bind(container.heightProperty());
		foreground.prefWidthProperty().bind(container.widthProperty());
		foreground.prefHeightProperty().bind(container.heightProperty());
		
		Platform.runLater(() -> {
			searchParent.requestFocus();
		});
	}
	
	private void enableDragging() {
		final Scene scene = container.getScene();
		scene.setOnDragOver(ev -> handleOver(ev));
		scene.setOnDragDropped(ev -> handleDrop(ev, f -> showUpload(f)));
	}
	
	private void whenLoggedIn() {
		//buttonProfile.se
		client.browse().stream().forEachOrdered(img -> 
			showImage(img)
		);
		foreground.setVisible(false);
		background.setEffect(null);
		enableDragging();
	}
	
	public void showLogin() {
		showLogin("", "");
	}
	
	public void showRegister() {
		showRegister("", "");
	}
	
	public void showLogin(String mail, String password) {
		final LoginController controller = new LoginController(client);
		final VBox popup = showFXMLPopup("Login.fxml", controller);
		controller.setMail(mail);
		controller.setPassword(password);
		
		controller.onLogin(m -> {
			FadeAnimation.fadeOut(popup, 
				ev -> container.getChildren().remove(popup)
			);
			whenLoggedIn();
		});

		controller.onShowRegister((m, p) -> {
			FadeAnimation.fadeOut(popup, 
				ev -> container.getChildren().remove(popup)
			);
			showRegister(m, p);
		});
	}

	public void showRegister(String mail, String password) {
		final RegisterController controller = new RegisterController(client);
		final VBox popup = showFXMLPopup("Register.fxml", controller);
		
		controller.setMail(mail);
		controller.setPassword(password);
		
		controller.onRegister(success -> {
			FadeAnimation.fadeOut(popup, 
				ev -> container.getChildren().remove(popup)
			);
			whenLoggedIn();
		});

		controller.onCancel((m, p) -> {
			FadeAnimation.fadeOut(popup, 
				ev -> container.getChildren().remove(popup)
			);
			showLogin(m, p);
		});
	}
	
	public void showUpload(File file) {
		final UploadController controller = new UploadController(client);
		final VBox popup = showFXMLPopup("Upload.fxml", controller);
		
		controller.onUpload(success -> {
			if (success) {
				FadeAnimation.fadeOut(popup, ev -> {
					container.getChildren().remove(popup);
					client.browse();
				});
			} else {
				controller.setError("Error! Upload failed.");
			}
		});
		
		controller.onCancel(success -> {
			FadeAnimation.fadeOut(popup, ev -> {
					container.getChildren().remove(popup);
				}
			);
		});
		
		controller.loadFile(file);
	}
	
	public void showProfile(File file) {
		final ProfileController controller = new ProfileController(client);
		final VBox popup = showFXMLPopup("Profile.fxml", controller);
		
		controller.onSave(usr -> {
			FadeAnimation.fadeOut(popup, ev -> {
				container.getChildren().remove(popup);
			});
		});
		
		controller.onCancel(success -> {
			FadeAnimation.fadeOut(popup, ev -> {
					container.getChildren().remove(popup);
				}
			);
		});
		
		controller.loadFile(file);
	}
	
	private VBox showFXMLPopup(String name, Object controller) {
		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH + "/fxml/" + name));
			loader.setController(controller);
			final VBox box = (VBox) loader.load();

			box.setOpacity(0);
			container.getChildren().add(box);
			LayoutUtil.centerInParent(box);
			FadeAnimation.fadeIn(box);
			return box;
		} catch (IOException ex) {
			Logger.getLogger(SceneController.class.getName()).log(
				Level.SEVERE, "Could not find '" + name + "'.", ex
			);
		}
		
		return null;
	}
	
	public void showImage(JSONImage img) {
		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH + "/fxml/Picture.fxml"));
			final PictureController controller = new PictureController(client);
			loader.setController(controller);
			final StackPane box = (StackPane) loader.load();
			controller.fromJSON(img);
			tilepanel.getChildren().add(box);
			
		} catch (IOException ex) {
			Logger.getLogger(SceneController.class.getName()).log(
				Level.SEVERE, "Could not find 'Picture.fxml'.", ex
			);
		}
	}
	
}