package com.speedment.examples.polaroid;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Emil Forslund
 */
public class JSONImage implements Comparable<JSONImage> {
	private String title, description;
	private Image image;
	private LocalDateTime uploaded;
	private JSONUser uploadedBy;
	
	private JSONImage() {}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Image getImage() {
		return image;
	}
	
	public LocalDateTime getUploaded() {
		return uploaded;
	}
	
	public JSONUser getUploadedBy() {
		return uploadedBy;
	}
	
	public static List<JSONImage> parseFrom(String json) {
		final JSONObject container = (JSONObject) JSONValue.parse(json);
		final JSONArray array = (JSONArray) container.get("images");
		final List<JSONImage> images = new ArrayList<>();
		
		array.stream().forEach(o -> {
			final JSONObject obj = (JSONObject) o;
			final JSONImage img = new JSONImage();
			img.title		= obj.get("title").toString();
			img.description = obj.get("description").toString();
			img.uploaded	= LocalDateTime.parse(obj.get("uploaded").toString());
			img.uploadedBy  = JSONUser.parse((JSONObject) obj.get("uploadedby"));
			img.image       = fromBase64(obj.get("imgdata").toString());
			images.add(img);
		});
		
		return images;
	}
	
	private static Image fromBase64(String input) {
		final byte[] data = Base64.decode(input);
		final ByteArrayInputStream bis = new ByteArrayInputStream(data);

		try {
			return SwingFXUtils.toFXImage(ImageIO.read(bis), null);
		} catch (IOException ex) {
			Logger.getLogger(JSONImage.class.getName()).log(Level.SEVERE, 
				"Failed to parse Base64-string to JavaFX-image.", ex);
		}
		
		return null;
	}
	
	public static String toBase64(File file) {
		try {
			return Base64.encode(Files.readAllBytes(file.toPath()));
		} catch (IOException ex) {
			Logger.getLogger(JSONImage.class.getName())
				.log(Level.SEVERE, "Could not read image '" + file.getName() + "'.", ex);
			return null;
		}
	}

	@Override
	public int compareTo(JSONImage o) {
		return (int) Duration.between(uploaded, o.uploaded).getSeconds();
	}
}