/**
 *
 * Copyright (c) 2006-2015, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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
import java.util.stream.Collectors;
import static javafx.animation.Animation.INDEFINITE;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import javafx.util.Duration;

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
		browseAndAppend();
		buttonProfile.setOnAction(ev -> showProfile());
		
		foreground.setVisible(false);
		background.setEffect(null);
		enableDragging();
		
		final Timeline refresh = new Timeline(new KeyFrame(Duration.seconds(10), ev -> 
			browseAndAppend()
		));
		
		refresh.setCycleCount(INDEFINITE);
		refresh.play();
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
					browseAndAppend();
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
	
	public void showProfile() {
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
			});
		});
	}
	
	public void showPicture(JSONImage img) {
		final PictureController controller = new PictureController(img);
		final VBox popup = showFXMLPopup("Picture.fxml", controller);
		
		controller.onCancel(success -> {
			FadeAnimation.fadeOut(popup, ev -> {
				container.getChildren().remove(popup);
			});
		});
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
	
	public StackPane showImage(JSONImage img) {
		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH + "/fxml/Thumbnail.fxml"));
			final ThumbnailController controller = new ThumbnailController();
			loader.setController(controller);
			final StackPane box = (StackPane) loader.load();
			controller.onClick(this::showPicture);
			controller.fromJSON(img);
			return box;
		} catch (IOException ex) {
			Logger.getLogger(SceneController.class.getName()).log(
				Level.SEVERE, "Could not find 'Thumbnail.fxml'.", ex
			);
		}
		
		return null;
	}
	
	public void browseAndAppend() {
		tilepanel.getChildren().addAll(0, 
			client.browse().stream()
			.map(i -> showImage(i))
			.collect(Collectors.toList())
		);
	}
}