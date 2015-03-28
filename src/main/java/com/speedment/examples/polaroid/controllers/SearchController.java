package com.speedment.examples.polaroid.controllers;

import com.speedment.examples.polaroid.ClientAPI;
import com.speedment.examples.polaroid.JSONUser;
import static com.speedment.examples.polaroid.MainApp.PATH;
import com.speedment.examples.polaroid.util.FadeAnimation;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControlBuilder;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Emil Forslund
 */
public class SearchController implements Initializable {
	
	@FXML private ScrollPane scroll;
	@FXML private VBox container;
	@FXML private Label searchingLabel;
	
	private final StringProperty input;
	private final ClientAPI client;
	private final ObservableDoubleValue x, y, width;

	public SearchController(ClientAPI client, StringProperty input, ObservableDoubleValue x, ObservableDoubleValue y, ObservableDoubleValue width) {
		this.client = client;
		this.input  = input;
		this.x		= x;
		this.y		= y;
		this.width  = width;
	}

	/**
	 * Initializes the controller class.
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		scroll.setOpacity(0);
		scroll.setVisible(false);
		scroll.layoutXProperty().bind(x);
		scroll.layoutYProperty().bind(y);
		scroll.prefWidthProperty().bind(width);
		scroll.prefViewportHeightProperty().bind(container.heightProperty());
		
		input.addListener((ob, o, n) -> {
			if ("".equals(n)) {
				FadeAnimation.fadeOut(scroll, e -> {
					scroll.setVisible(false);
					container.getChildren().add(searchingLabel);
				});
			} else {
				if (!scroll.isVisible()) {
					scroll.setOpacity(0);
					scroll.setVisible(true);
					FadeAnimation.fadeIn(scroll);
				}

				final List<JSONUser> users = client.find(n);
				container.getChildren().clear();
				users.forEach(u -> SearchResultController.showIn(
					container.getChildren(), u, client
				));
			}
		});
	}
	
	public static ScrollPane showIn(ObservableList<Node> siblings, TextField owner, ClientAPI client) {

		final SearchController controller = new SearchController(client,
			owner.textProperty(),
			owner.layoutXProperty(),
			owner.layoutYProperty().add(owner.heightProperty()),
			owner.widthProperty()
		);

		try {
			final FXMLLoader loader = new FXMLLoader(
				SearchController.class.getResource(
					PATH + "/fxml/Search.fxml"
				)
			);
			loader.setController(controller);
			final ScrollPane box = (ScrollPane) loader.load();
			siblings.add(box);
			return box;
		} catch (IOException ex) {
			Logger.getLogger(SearchResultController.class.getName()).log(
				Level.SEVERE, "Could not find 'SearchResult.fxml'.", ex
			);
		}
		
		return null;
	}
}