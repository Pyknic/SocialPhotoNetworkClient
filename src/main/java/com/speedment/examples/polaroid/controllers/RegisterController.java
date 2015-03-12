/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.speedment.examples.polaroid.controllers;

import com.speedment.examples.polaroid.Client;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Emil Forslund
 */
public class RegisterController implements Initializable {
	@FXML private TextField fieldMail;
	@FXML private PasswordField fieldPassword;
	@FXML private PasswordField fieldPasswordRepeat;
	@FXML private Button buttonCancel;
	@FXML private Button buttonRegister;
	@FXML private Label labelError;
	
	private BiConsumer<String, String> registerListener;
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
			final String mail = fieldMail.textProperty().getValue();
			final String password = fieldPassword.textProperty().getValue();
			final String password2 = fieldPasswordRepeat.textProperty().getValue();
			
			if (password.equals(password2)) {
				if (registerListener != null) {
					registerListener.accept(mail, password);
				} else {
					throw new UnsupportedOperationException(
						"No RegisterListener is set in RegisterController."
					);
				}
			} else {
				showError("Passwords doesn't match.");
			}
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
	
	public void onRegister(BiConsumer<String, String> listener) {
		registerListener = listener;
	}
	
	public void setMail(String mail) {
		fieldMail.textProperty().setValue(mail);
	}
	
	public void setPassword(String password) {
		fieldPassword.textProperty().setValue(password);
	}
	
	public void showError(String msg) {
		labelError.setVisible(true);
		labelError.setText(msg);
	}
}