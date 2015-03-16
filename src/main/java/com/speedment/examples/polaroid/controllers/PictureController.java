package com.speedment.examples.polaroid.controllers;

import com.speedment.examples.polaroid.Client;
import com.speedment.examples.polaroid.JSONImage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Emil Forslund
 */
public class PictureController implements Initializable {
	
	@FXML private ImageView picture;
	@FXML private Label title;
	@FXML private Button heart;
	@FXML private ImageView profile;
	
	private final Client client;
	
	public PictureController(Client client) {
		this.client = client;
	}
	
	public void fromJSON(JSONImage img) {
		picture.setImage(img.getImage());
		title.textProperty().setValue(img.getTitle());
	}

	/**
	 * Initializes the controller class.
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}
}