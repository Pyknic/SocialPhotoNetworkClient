package com.speedment.examples.polaroid;

import static com.speedment.examples.polaroid.util.Base64Util.fromBase64;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
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

	@Override
	public int compareTo(JSONImage o) {
		return (int) Duration.between(uploaded, o.uploaded).getSeconds();
	}
}