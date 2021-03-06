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

import com.speedment.examples.social.ClientAPI;
import com.speedment.examples.social.JSONUser;
import static com.speedment.examples.social.MainApp.PATH;
import static com.speedment.examples.social.util.Avatar.DEFAULT_AVATAR_IMG;
import com.speedment.examples.social.util.FadeAnimation;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Emil Forslund
 */
public class SearchResultController implements Initializable {
	
	@FXML private HBox container;
	@FXML private ImageView profileSmall;
	@FXML private Label firstAndLastname;
	@FXML private Label mail;
	@FXML private Button buttonFollow;
	
	private final JSONUser user;
	private final ClientAPI client;
	
	public SearchResultController(JSONUser user, ClientAPI client) {
		this.user   = user;
		this.client = client;
	}

	/**
	 * Initializes the controller class.
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		firstAndLastname.setText(user.getFirstname() + " " + user.getLastname());
		mail.setText(user.getMail());
		
		if (user.getAvatar() == null) {
			profileSmall.setImage(DEFAULT_AVATAR_IMG);
		} else {
			profileSmall.setImage(user.getAvatar());
		}
		
		buttonFollow.setOnAction(ev -> {
			buttonFollow.setDisable(true);
			
			if (client.follow(user.getId())) {
				final Label success = new Label("Followed!");
				success.setStyle("-fx-text-fill: rgb(200, 255, 160);");
				success.setOpacity(0);
				FadeAnimation.fadeOut(buttonFollow, e -> {
					container.getChildren().remove(buttonFollow);
					container.getChildren().add(success);
					FadeAnimation.fadeIn(success);
				});
			} else {
				final Label success = new Label("Already followed!");
				success.setStyle("-fx-text-fill: rgb(255, 200, 160);");
				success.setOpacity(0);
				FadeAnimation.fadeOut(buttonFollow, e -> {
					container.getChildren().remove(buttonFollow);
					container.getChildren().add(success);
					FadeAnimation.fadeIn(success);
				});
			}
		});
	}
	
	public static HBox showIn(ObservableList<Node> siblings, JSONUser user, ClientAPI client) {
		try {
			final FXMLLoader loader = new FXMLLoader(SearchResultController.class.getResource(PATH + "/fxml/SearchResult.fxml"));
			final SearchResultController controller = new SearchResultController(user, client);
			loader.setController(controller);
			final HBox box = (HBox) loader.load();
			siblings.add(0, box);
			return box;
		} catch (IOException ex) {
			Logger.getLogger(SearchResultController.class.getName()).log(
				Level.SEVERE, "Could not find 'SearchResult.fxml'.", ex
			);
		}
		
		return null;
	}
}