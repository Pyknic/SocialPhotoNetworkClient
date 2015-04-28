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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.speedment.examples.social.controllers;

import com.speedment.examples.social.Client;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Emil Forslund
 */
public class RegisterController implements Initializable {
	
	@FXML private VBox container;
	@FXML private TextField fieldMail;
	@FXML private PasswordField fieldPassword;
	@FXML private PasswordField fieldPasswordRepeat;
	@FXML private Button buttonCancel;
	@FXML private Button buttonRegister;
	@FXML private Label labelError;
	
	private Consumer<String> registerListener;
	private BiConsumer<String, String> cancelListener;
	private final Client client;
	
	public RegisterController(Client client) {
		this.client = client;
	}

	/**
	 * Initializes the controller class.
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		labelError.setVisible(false);
		
		fieldMail.textProperty().addListener(ev -> {
			labelError.setVisible(false);
		});
		
		fieldPassword.textProperty().addListener(ev -> {
			labelError.setVisible(false);
		});
		
		fieldPasswordRepeat.textProperty().addListener(ev -> {
			labelError.setVisible(false);
		});
		
		buttonRegister.setOnAction(ev -> {
			buttonRegister.setDisable(true);
			final String mail = fieldMail.textProperty().getValue();
			final String password = fieldPassword.textProperty().getValue();
			final String password2 = fieldPasswordRepeat.textProperty().getValue();
			
			if (password.equals(password2)) {
				if (registerListener != null) {
					if (client.register(mail, password)) {
						registerListener.accept(mail);
					} else {
						showError("Could not register with those credentials.");
					}
				} else {
					throw new UnsupportedOperationException(
						"No RegisterListener is set in RegisterController."
					);
				}
			} else {
				showError("Passwords doesn't match.");
			}
			
			buttonRegister.setDisable(false);
		});
		
		buttonCancel.setOnAction(ev -> {
			if (cancelListener != null) {
				cancelListener.accept(
					fieldMail.textProperty().getValue(), 
					fieldPassword.textProperty().getValue()
				);
			} else {
				throw new UnsupportedOperationException(
					"No CancelListener is set in RegisterController."
				);
			}
		});
	}
	
	public void onCancel(BiConsumer<String, String> listener) {
		cancelListener = listener;
	}
	
	public void onRegister(Consumer<String> listener) {
		registerListener = listener;
	}
	
	public void setMail(String mail) {
		fieldMail.textProperty().setValue(mail);
	}
	
	public void setPassword(String password) {
		fieldPassword.textProperty().setValue(password);
	}
	
	public void showError(String msg) {
		DialogController.showDialog(
			(Pane) container.getParent(), 
			"Registration error", msg
		);
	}
}