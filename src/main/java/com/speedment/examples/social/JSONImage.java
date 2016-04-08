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

import com.speedment.examples.social.util.Base64Util;
import java.time.Instant;
import javafx.scene.image.Image;

/**
 *
 * @author Emil Forslund
 */
public class JSONImage implements Comparable<JSONImage> {
    
	private String title, description, data;
	private long uploaded;
	private JSONUser uploader;
	
	private JSONImage() {}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Image getImage() {
		return Base64Util.fromBase64(data);
	}
	
	public Instant getUploaded() {
		return Instant.ofEpochMilli(uploaded);
	}
	
	public JSONUser getUploader() {
		return uploader;
	}

	@Override
	public int compareTo(JSONImage o) {
		return (int) ((o.uploaded - uploaded) / 1000);
	}
}