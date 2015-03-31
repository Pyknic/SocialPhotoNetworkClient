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
import static com.speedment.examples.social.util.DropHelper.handleDrop;
import static com.speedment.examples.social.util.DropHelper.handleOver;
import com.speedment.examples.social.util.ImageResizeUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Emil Forslund
 */
public class UploadController implements Initializable {
	
	@FXML private TextField fieldTitle;
	@FXML private ImageView fieldImage;
	@FXML private TextArea fieldDescription;
	@FXML private Button buttonCancel;
	@FXML private Button buttonUpload;
	@FXML private Label labelError;
	
	private final ClientAPI client;
	private Consumer<Boolean> onUpload;
	private Consumer<Boolean> onCancel;
	private File lastFile;
	
	public UploadController(ClientAPI client) {
		this.client = client;
	}

	/**
	 * Initializes the controller class.
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		fieldImage.setOnDragOver(ev -> handleOver(ev));
		
		fieldImage.setOnDragDropped(ev -> handleDrop(ev, f -> loadFile(f)));
		
		buttonUpload.setOnAction(ev -> {
			labelError.setVisible(false);
			buttonUpload.setDisable(true);
			onUpload.accept(client.upload(
				fieldTitle.getText(), 
				fieldDescription.getText(),
				ImageResizeUtil.loadAndEncode(lastFile, 256, 256)
			));
		});
		
		buttonCancel.setOnAction(ev -> {
			labelError.setVisible(false);
			onCancel.accept(true);
		});
		
		labelError.setVisible(false);
	}
	
	public void onUpload(Consumer<Boolean> listener) {
		onUpload = listener;
	}
	
	public void onCancel(Consumer<Boolean> listener) {
		onCancel = listener;
	}
	
	public void setError(String msg) {
		labelError.setText(msg);
		labelError.setVisible(true);
		buttonUpload.setDisable(false);
	}
	
	public void setTitle(String title) {
		fieldTitle.setText(title);
	}
	
	public void loadFile(File file) {
		try {
			final Image img = new Image(new FileInputStream(file));
			fieldImage.setImage(img);
			lastFile = file;
		} catch (FileNotFoundException ex) {
			throw new RuntimeException("File '" + file.getName() + "' could not be found.");
		}
	}
}
