package com.speedment.examples.polaroid.util;

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
