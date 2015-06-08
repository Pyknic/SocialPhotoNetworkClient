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
package com.speedment.examples.social.util;

import com.speedment.examples.social.JSONImage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author Emil Forslund
 */
public final class Base64Util {
	
	public static Image fromBase64(String input) {
		final byte[] data = Base64.getDecoder().decode(input);
		final ByteArrayInputStream bis = new ByteArrayInputStream(data);

		try {
            final BufferedImage img = ImageIO.read(bis);
            if (img == null) {
                Logger.getLogger(JSONImage.class.getName()).log(Level.SEVERE, 
                    "Failed to parse Base64-string to JavaFX-image.");
            } else {
                return SwingFXUtils.toFXImage(img, null);
            }
		} catch (IOException ex) {
			Logger.getLogger(JSONImage.class.getName()).log(Level.SEVERE, 
				"Failed to parse Base64-string to JavaFX-image.", ex);
		}
		
		return null;
	}
	
	public static String toBase64(File file) {
		try {
			return Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
		} catch (IOException ex) {
			Logger.getLogger(JSONImage.class.getName())
				.log(Level.SEVERE, "Could not read image '" + file.getName() + "'.", ex);
			return null;
		}
	}
}