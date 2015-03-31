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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emil Forslund
 */
public class FileUtil {
	public static void create(String filename, String data) {
		try {
			File file = new File(filename);

			if (!file.exists()) {
				file.createNewFile();
			}

			final FileWriter fw = new FileWriter(file.getAbsoluteFile());
			try (BufferedWriter bw = new BufferedWriter(fw)) {
				bw.write(data);
			}
		} catch (IOException ex) {
			Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
