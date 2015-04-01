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
package com.speedment.examples.social;

import static com.speedment.examples.social.util.Base64Util.fromBase64;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	private JSONUser uploader;
	
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
	
	public JSONUser getUploader() {
		return uploader;
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
			img.uploaded	= LocalDateTime.parse(obj.get("uploaded").toString().trim().replace(" ", "T"));
			img.uploader  = JSONUser.parse((JSONObject) obj.get("uploader"));
			img.image       = fromBase64(obj.get("imgData").toString());
			images.add(img);
		});
		
		Collections.sort(images, Comparator.reverseOrder());
		
		return images;
	}

	@Override
	public int compareTo(JSONImage o) {
		return (int) Duration.between(o.uploaded, uploaded).getSeconds();
	}
}