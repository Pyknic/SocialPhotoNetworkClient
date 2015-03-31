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

import com.speedment.examples.social.JSONImage;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import static javafx.animation.Interpolator.EASE_BOTH;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Emil Forslund
 */
public class ThumbnailController implements Initializable {
	
	@FXML private StackPane container;
	@FXML private ImageView picture;
	@FXML private Label title;

	private JSONImage img;
	private Consumer<JSONImage> clickListener;

	/**
	 * Initializes the controller class.
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		final KeyFrame 
			kfFocus0 = frame(0, 1),
			kfFocus1 = frame(0.1, 1.05),
			kfBlur0  = frame(0, 1.05),
			kfBlur1  = frame(0.1, 1);

		final Timeline 
			focus = new Timeline(kfFocus0, kfFocus1),
			blur  = new Timeline(kfBlur0, kfBlur1);

		picture.setOnMouseEntered(ev -> focus.play());
		picture.setOnMouseExited(ev -> blur.play());
		picture.setOnMousePressed(ev -> {
			if (ev.isPrimaryButtonDown()) {
				clickListener.accept(img);
			}
		});
	}
	
	public void fromJSON(JSONImage img) {
		this.img = img;
		
		picture.setImage(img.getImage());
		title.textProperty().setValue(img.getTitle());
	}
	
	public void onClick(Consumer<JSONImage> listener) {
		clickListener = listener;
	}
	
	private KeyFrame frame(double seconds, double scale) {
		return new KeyFrame(Duration.seconds(seconds),
			new KeyValue(container.scaleXProperty(), scale, EASE_BOTH),
			new KeyValue(container.scaleYProperty(), scale, EASE_BOTH)
		);
	}
}