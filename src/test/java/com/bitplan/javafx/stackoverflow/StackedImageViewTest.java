/**
 *
 * This file is part of the https://github.com/BITPlan/com.bitplan.javafx open source project
 *
 * The copyright and license below holds true
 * for all files except
 *    - the ones in the stackoverflow package
 *    - SwingFXUtils.java from Oracle which is provided e.g. for the raspberry env
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

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * How do I center an Image view in an anchor pane?
 * https://stackoverflow.com/a/43537764/1497139
 * @author wf
 *
 */
public class StackedImageViewTest extends TestApplication {

  @Override
  public void start(Stage primaryStage)  {
    ImageView imageView = super.getImageView(primaryStage);
    StackPane pane = new StackPane();
    pane.getChildren().add(imageView);
    StackPane.setAlignment(imageView, Pos.CENTER);
    createSceneAndShowStage(primaryStage, pane);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
