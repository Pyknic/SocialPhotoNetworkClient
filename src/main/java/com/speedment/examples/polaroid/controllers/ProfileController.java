package com.speedment.examples.polaroid.controllers;

import com.speedment.examples.polaroid.Client;
import com.speedment.examples.polaroid.JSONUser;
import static com.speedment.examples.polaroid.util.DropHelper.handleDrop;
import static com.speedment.examples.polaroid.util.DropHelper.handleOver;
import com.speedment.examples.polaroid.util.FadeAnimation;
import com.speedment.examples.polaroid.util.ImageResizeUtil;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Emil Forslund
 */
public class ProfileController implements Initializable {
	
	@FXML private VBox container;
	@FXML private ImageView profile;
	@FXML private TextField fieldFirstname;
	@FXML private TextField fieldLastname;
	@FXML private TextField fieldMail;
	@FXML private Label labelError;
	@FXML private Button buttonCancel;
	@FXML private Button buttonSave;
	
	private final JSONUser user;
	private final Client client;
	
	private Consumer<JSONUser> saveListener;
	private Consumer<Boolean> cancelListener;
	
	private File lastFile;
	
	public ProfileController(Client client) {
		this.user   = client.self();
		this.client = client;
	}

	/**
	 * Initializes the controller class.
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		if (user != null) {
			if (user.getAvatar() != null) {
				profile.setImage(user.getAvatar());
			}
			
			fieldFirstname.setText(user.getFirstname());
			fieldLastname.setText(user.getLastname());
			fieldMail.setText(user.getMail());
		}
		
		buttonCancel.setOnAction(ev -> {
			if (cancelListener != null) {
				cancelListener.accept(true);
			}
		});
		
		buttonSave.setOnAction(ev -> {
			final String imgData;
			
			if (lastFile != null) {
				imgData = ImageResizeUtil.loadAndEncode(lastFile, 256, 256);
			} else {
				imgData = "";
			}
			
			final JSONUser usr = client.update(
				fieldMail.getText(), 
				fieldFirstname.getText(), 
				fieldLastname.getText(), 
				imgData
			);
			
			if (usr != null && saveListener != null) {
				saveListener.accept(usr);
			} else {
				setError("Update was denied!");
			}
		});
		
		profile.setOnDragOver(ev -> handleOver(ev));
		profile.setOnDragDropped(ev -> handleDrop(ev, f -> loadFile(f)));
	}
	
	public void setError(String msg) {
		labelError.setText(msg);
		labelError.setVisible(true);
	}
	
	public void onSave(Consumer<JSONUser> listener) {
		saveListener = listener;
	}
	
	public void onCancel(Consumer<Boolean> listener) {
		cancelListener = listener;
	}
	
	public void loadFile(File file) {
		try {
			final Image img = new Image(new FileInputStream(file));
			profile.setImage(img);
			lastFile = file;
		} catch (FileNotFoundException ex) {
			throw new RuntimeException("File '" + file.getName() + "' could not be found.");
		}
	}
}