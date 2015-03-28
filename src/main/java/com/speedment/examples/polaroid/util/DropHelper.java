package com.speedment.examples.polaroid.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import static javafx.scene.input.TransferMode.COPY;

/**
 *
 * @author Emil Forslund
 */
public class DropHelper {
	private final static List<String> ACCEPTED_EXTENSIONS = Arrays.asList(new String[] {
		"png", "jpg", "bmp", "gif", "jpeg"
	});
	
	public static void handleOver(DragEvent ev) {
		final Dragboard db = ev.getDragboard();

		if (db.hasFiles()) {
			ev.acceptTransferModes(COPY);
		} else {
			ev.consume();
		}
	}
	
	public static void handleDrop(DragEvent ev, Consumer<File> ifValid) {
		final Dragboard db = ev.getDragboard();
		boolean success = false;

		if (db.hasFiles()) {
			success = true;

			db.getFiles().stream()
				.filter(f -> f.isFile())
				.filter(f -> ACCEPTED_EXTENSIONS.contains(extensionOf(f)))
				.findFirst()
				.ifPresent(f -> ifValid.accept(f));
		}

		ev.setDropCompleted(success);
		ev.consume();
	}
	
	private static String extensionOf(File file) {
		final int i = file.getName().lastIndexOf(".");
		if (i > 0) {
			return file.getName().substring(i + 1);
		} else {
			return "";
		}
	}
}
