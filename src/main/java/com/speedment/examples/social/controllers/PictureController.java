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
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Emil Forslund
 */
public class PictureController implements Initializable {
	
	@FXML private VBox container;
	@FXML private Label title;
	@FXML private ImageView picture;
	@FXML private Label description;
	@FXML private Label meta;
	@FXML private Button buttonCancel;
	
	private final JSONImage img;
	private Consumer<Boolean> cancelListener;
	
	public PictureController(JSONImage img) {
		this.img = img;
	}

	/**
	 * Initializes the controller class.
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		title.setText(img.getTitle());
		description.setText(img.getDescription());
		picture.setImage(img.getImage());
		meta.setText("Uploaded " + 
			img.getUploaded().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
				.replace("T", " ") + 
			" by " + img.getUploadedBy().getFirstname() + 
			" " + img.getUploadedBy().getLastname() + "."
		);
		
		buttonCancel.setOnAction(ev -> {
			if (cancelListener != null) {
				cancelListener.accept(true);
			}
		});
	}	
	
	public void onCancel(Consumer<Boolean> listener) {
		cancelListener = listener;
	}
}