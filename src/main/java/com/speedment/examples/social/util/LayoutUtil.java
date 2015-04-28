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

import javafx.scene.Parent;
import javafx.scene.layout.Region;

/**
 *
 * @author Emil Forslund
 */
public class LayoutUtil {
	
	public static void centerInParent(Region child) {
		final Parent parent = child.getParent();
		
		if (parent instanceof Region) {
			final Region parentRegion = (Region) parent;
			
			child.layoutXProperty().bind(
				parentRegion.widthProperty().divide(2).subtract(
					child.widthProperty().divide(2)
				)
			);
			
			child.layoutYProperty().bind(
				parentRegion.heightProperty().divide(2).subtract(
					child.heightProperty().divide(2)
				)
			);
		} else {
			throw new UnsupportedOperationException("Parent " + parent + " is not a Region.");
		}
		
	}
}