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
package com.speedment.examples.polaroid.util;

import com.speedment.examples.polaroid.JSONImage;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 *
 * @author Emil Forslund
 */
public final class ImageResizeUtil {
	public static String loadAndEncode(File imgFile, int maxWidth, int maxHeight) {
		try {
			final BufferedImage original = ImageIO.read(imgFile);
			final BufferedImage resized = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
			final Graphics2D g = resized.createGraphics();
			
			final Image temp;
			if (original.getWidth() > original.getHeight()) {
				temp = original.getScaledInstance(-1, maxHeight, Image.SCALE_SMOOTH);
			} else {
				temp = original.getScaledInstance(maxWidth, -1, Image.SCALE_SMOOTH);
			}

			g.drawImage(temp, 
				(int) (maxWidth * 0.5 - temp.getWidth(null) * 0.5), 
				(int) (maxHeight * 0.5 - temp.getHeight(null) * 0.5),
				null
			);

			byte[] bytes;
			
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				ImageIO.write(resized, "jpg", baos);
				baos.flush();
				bytes = baos.toByteArray();
			}
			
			g.dispose();
			
			return Base64.encode(bytes);
			
		} catch (IOException ex) {
			Logger.getLogger(JSONImage.class.getName())
				.log(Level.SEVERE, "Could not read image '" + imgFile.getName() + "'.", ex);
			return null;
		}
	}
}
