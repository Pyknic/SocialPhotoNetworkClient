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
package com.speedment.examples.social.controllers;

import com.speedment.examples.social.Client;
import com.speedment.examples.social.JSONImage;
import com.speedment.examples.social.JSONUser;
import static com.speedment.examples.social.MainApp.PATH;
import com.speedment.examples.social.Settings;
import static com.speedment.examples.social.controllers.DialogController.showDialog;
import static com.speedment.examples.social.util.Avatar.DEFAULT_AVATAR_IMG;
import static com.speedment.examples.social.util.DropHelper.handleOver;
import com.speedment.examples.social.util.FadeAnimation;
import com.speedment.examples.social.util.LayoutUtil;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static javafx.animation.Animation.INDEFINITE;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import static com.speedment.examples.social.util.DropHelper.handleDrop;

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
		this.client = new Client(
			Settings.inst().get("host", "127.0.0.1"),
			Integer.parseInt(Settings.inst().get("port", "8080")),
			t -> showDialog(container, t.getClass().getSimpleName(), t.getMessage())
		);
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
	
	///////////////////////////////////////////////////////////////
	//                    Navigation methods.                    //
	///////////////////////////////////////////////////////////////
	
	public void showLogin(String mail, String password) {
		final LoginController controller = new LoginController(client);
		final Consumer<Consumer<VBox>> closeHandler = showFXMLPopup("Login.fxml", controller, false);
		controller.setMail(mail);
		controller.setPassword(password);
		
		controller.onLogin(m -> closeHandler.accept(b -> 
			whenLoggedIn()
		));

		controller.onShowRegister((m, p) -> closeHandler.accept(b -> 
			showRegister(m, p)
		));
	}

	public void showRegister(String mail, String password) {
		final RegisterController controller = new RegisterController(client);
		final Consumer<Consumer<VBox>> closeHandler = showFXMLPopup("Register.fxml", controller, false);
		
		controller.setMail(mail);
		controller.setPassword(password);
		
		controller.onRegister(success -> {
			closeHandler.accept(b -> {});
			whenLoggedIn();
		});
		
		controller.onCancel((m, p) -> 
			closeHandler.accept(b -> 
				showLogin(m, p)
			)
		);
	}
	
	public void showUpload(File file) {
		final UploadController controller = new UploadController(client);
		final Consumer<Consumer<VBox>> closeHandler = showFXMLPopup("Upload.fxml", controller, true);
		controller.setTitle(file.getName());
		
		controller.onUpload(success -> {
			if (success) {
				closeHandler.accept(b -> 
					browseAndAppend()
				);
			} else {
				controller.setError("Error! Upload failed.");
			}	
		});
		
		controller.onCancel(s -> closeHandler.accept(b -> {}));
		controller.loadFile(file);
	}
	
	public void showProfile(JSONUser user) {
		final ProfileController controller = new ProfileController(user, client);
		final Consumer<Consumer<VBox>> closeHandler = showFXMLPopup("Profile.fxml", controller, true);
		
		controller.onSave(usr -> 
			closeHandler.accept(b -> updateProfileButton())
		);
		
		controller.onCancel(success -> 
			closeHandler.accept(b -> {})
		);
	}
	
	public void showPicture(JSONImage img) {
		foreground.setVisible(false);
		container.getChildren().add(foreground);
		
		final PictureController controller = new PictureController(img);
		final Consumer<Consumer<VBox>> closeHandler = showFXMLPopup("Picture.fxml", controller, true);

		controller.onCancel(success -> closeHandler.accept(b -> {}));
	}
	
	/**
	 * Show an internal popup in this scene. It can either be a closeable popup
	 * (like a warning message) or a non-closeable popup (like the login screen).
	 * The popup will blur out all other components.
	 * 
	 * The resulting closeHandler is a consumer of a consumer of a box. It can
	 * be used to close the popup automatically when the user pressed a certain
	 * button. To close the popup, call the <code>accept()</code> method in the
	 * consumer. The input parameter should be a consumer that will be called
	 * when the fadeOut has finished and the popup is gone. This can be empty.
	 * 
	 * @param name The name of the .fxml file. (ex: 'Scene.fxml').
	 * @param controller The object that will act as controller.
	 * @param closeable If the popup should be closeable by the user.
	 * @return close handler.
	 */
	private Consumer<Consumer<VBox>> showFXMLPopup(String name, Object controller, boolean closeable) {
		foreground.setVisible(false);
		
		if (!container.getChildren().contains(foreground)) {
			container.getChildren().add(foreground);
		}

		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH + "/fxml/" + name));
			loader.setController(controller);
			final VBox box = (VBox) loader.load();

			box.setOpacity(0);
			container.getChildren().add(box);
			LayoutUtil.centerInParent(box);
			FadeAnimation.fadeIn(box);
			
			foreground.setOpacity(0);
			foreground.setStyle("-fx-background-color:rgba(0,0,0,0.5);");
			foreground.setVisible(true);
			
			final Consumer<Consumer<VBox>> closeHandler = c -> {
				FadeAnimation.fadeOut(box, e -> {
					container.getChildren().remove(box);
				});
				FadeAnimation.fadeOut(foreground, e -> {
					container.getChildren().remove(foreground);
				});
				c.accept(box);
			};
			
			if (closeable) {
				foreground.setOnMousePressed(ev -> closeHandler.accept(b -> {}));
			} else {
				foreground.setOnMousePressed(null);
			}
			FadeAnimation.fadeIn(foreground);
			
			return closeHandler;
		} catch (IOException ex) {
			Logger.getLogger(SceneController.class.getName()).log(
				Level.SEVERE, "Could not find '" + name + "'.", ex
			);
		}
		
		return c -> {};
	}
	
	private void browseAndAppend() {
		client.browse().thenAccept(list -> {
            tilepanel.getChildren().addAll(0, 
                list.stream()
                    .map(this::createThumbnailFrom)
                    .map(i -> {
                        i.setRotate(Math.random() * 10 - 5);
                        return i;
                    })
                    .collect(Collectors.toList())
            );
        }).handleAsync((v, ex) -> {
            if (ex != null) {
                DialogController.showDialog(container, 
                    "Failed to browse", 
                    "This could mean that the server was gone offline or that the " +
                    "specified command is not yet implemented."
                );
            }

            return v;
        });
	}
	
	private StackPane createThumbnailFrom(JSONImage img) {
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
	
	private void enableDragging() {
		final Scene scene = container.getScene();
		scene.setOnDragOver(ev -> handleOver(ev));
		scene.setOnDragDropped(ev -> handleDrop(ev, f -> showUpload(f), true));
	}
	
	private void whenLoggedIn() {
		browseAndAppend();
		container.getChildren().remove(foreground);
		background.setEffect(null);
		enableDragging();
		updateProfileButton();
		
		final Timeline refresh = new Timeline(new KeyFrame(Duration.seconds(10), ev -> 
			browseAndAppend()
		));
		
		refresh.setCycleCount(INDEFINITE);
		refresh.play();
	}
	
	private void updateProfileButton() {
		client.profile().thenAccept(profile -> {
            if (profile.isPresent()) {
                buttonProfile.setText(
                    profile.get().getFirstname() + " " + 
                    profile.get().getLastname()
                );

                final ImageView icon;
                if (profile.get().getAvatar() == null) {
                    icon = new ImageView(DEFAULT_AVATAR_IMG);
                } else {
                    icon = new ImageView(profile.get().getAvatar().getImage());
                }

                icon.setFitWidth(32);
                icon.setFitHeight(32);
                buttonProfile.setGraphic(icon);
                buttonProfile.setOnAction(ev -> showProfile(profile.get()));

                Settings.inst().set("username", profile.get().getUsername());
            }
        });
	}
}