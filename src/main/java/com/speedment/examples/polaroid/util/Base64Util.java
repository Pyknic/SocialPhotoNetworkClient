package com.speedment.examples.polaroid.util;

import com.speedment.examples.polaroid.JSONImage;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
}
