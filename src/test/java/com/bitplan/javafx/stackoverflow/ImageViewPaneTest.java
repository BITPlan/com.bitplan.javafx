/**
 *
 * This file is part of the https://github.com/BITPlan/com.bitplan.javafx open source project
 *
 * The copyright and license below holds true
 * for all files except the ones in the stackoverflow package
 *
 * Copyright 2017-2018 BITPlan GmbH https://github.com/BITPlan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *  You may obtain a copy of the License at
 *
 *  http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bitplan.javafx.stackoverflow;

import com.bitplan.javafx.ImageViewPane;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ImageViewPaneTest extends TestApplication {

  @Override
  public void start(Stage primaryStage) throws Exception {
    // get an imageView with the fitWidth / fitHeight not bound yet
    ImageView imageView = super.getImageView();
    ImageViewPane imageViewPane = new ImageViewPane(imageView);

    StackPane root = new StackPane();
    root.getChildren().add(imageViewPane);

    Scene scene = new Scene(root, 300, 250);

    primaryStage.setTitle("ImageViewPaneTest");
    primaryStage.setScene(scene);
    primaryStage.show();

  }

  public static void main(String[] args) {
    launch(args);
  }
}
