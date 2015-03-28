package com.speedment.examples.polaroid.util;

import com.speedment.examples.polaroid.JSONImage;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.Graphics2D;
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
			final double factor = Math.min(maxWidth, maxHeight) / 
				Math.min(original.getWidth(), original.getHeight());
			final BufferedImage resized = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
			final Graphics2D g = resized.createGraphics();
			
			g.drawImage(original, 
				(int) (maxWidth / 2 - original.getWidth() * factor / 2), 
				(int) (maxHeight / 2 - original.getHeight() * factor / 2), 
				maxWidth, maxHeight,
				0, 0, original.getWidth(), original.getHeight(),
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
