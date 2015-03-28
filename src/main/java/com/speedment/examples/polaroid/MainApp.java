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
package com.speedment.examples.polaroid;

import com.speedment.examples.polaroid.controllers.SceneController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	public final static String PATH = "/com/speedment/examples/polaroid";

    @Override
    public void start(Stage stage) throws Exception {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH + "/fxml/Scene.fxml"));
        final SceneController control = new SceneController(stage);
		loader.setController(control);
		
		final Parent root = (Parent) loader.load();
        final Scene scene = new Scene(root);
		
        scene.getStylesheets().add(PATH + "/styles/Styles.css");
        
        stage.setTitle("Polaroid Social Network");
        stage.setMaximized(true);
		stage.setScene(scene);
        stage.show();

		control.showLogin(Settings.inst().get("mail", ""), "");
    }

    public static void main(String[] args) {
        launch(args);
    }
}