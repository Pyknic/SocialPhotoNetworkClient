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

import static com.speedment.examples.social.MainApp.PATH;
import static com.speedment.examples.social.util.FadeAnimation.fadeIn;
import static com.speedment.examples.social.util.FadeAnimation.fadeOut;
import com.speedment.examples.social.util.LayoutUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Emil Forslund
 */
public class DialogController implements Initializable {
	
	@FXML private StackPane foreground;
	@FXML private VBox container;
	@FXML private Label title;
	@FXML private Label message;
	@FXML private Button buttonClose;
	
	private final String titleString, messageString;
	private final Consumer<StackPane> closeHandler;
	
	protected DialogController(String title, String message) {
		this (title, message, vb -> {});
	}
	
	protected DialogController(String title, String message, Consumer<StackPane> closeHandler) {
		this.titleString   = title;
		this.messageString = message;
		this.closeHandler  = closeHandler;
	}

	/**
	 * Initializes the controller class.
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		title.setText(titleString);
		message.setText(messageString);
		
		buttonClose.setOnAction(ev -> {
			closeHandler.accept(foreground);
		});
	}
	
	private final static String FXML_FILE = PATH + "/fxml/Dialog.fxml";
	
	public static Consumer<Consumer<StackPane>> showDialog(Pane parent, String title, String message) {
		final DialogController controller = new DialogController(title, message, sp -> {
			fadeOut(sp, e -> {
				parent.getChildren().remove(sp);
			});
		});
		
		try {
			final FXMLLoader loader = new FXMLLoader(DialogController.class.getResource(FXML_FILE));
			loader.setController(controller);
			final StackPane foreground = (StackPane) loader.load();

			foreground.setOpacity(0);
			parent.getChildren().add(foreground);
			LayoutUtil.fillParent(foreground);

			final Consumer<Consumer<StackPane>> closeHandler = c -> {
				fadeOut(foreground, e -> {
					parent.getChildren().remove(foreground);
				});
				c.accept(foreground);
			};
			
			foreground.setOnMousePressed(ev -> closeHandler.accept(sp -> {}));
			fadeIn(foreground);
			
			return closeHandler;
		} catch (IOException ex) {
			Logger.getLogger(DialogController.class.getName()).log(
				Level.SEVERE, "Could not find '" + FXML_FILE + "'.", ex
			);
		}
		
		return c -> {};
	}
}