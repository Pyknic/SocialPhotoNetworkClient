package com.speedment.examples.polaroid.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

	/**
	 * Initializes the controller class.
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
}