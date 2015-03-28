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
