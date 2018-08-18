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

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * javafx - access height and width values of an image after it is resized to
 * preserve aspect ratio https://stackoverflow.com/a/36291104/1497139
 * 
 * @author wf
 *
 */
public class ImageViewTest extends TestApplication {

  @Override
  public void start(Stage stage) throws Exception {

    ResizableCanvas rc = new ResizableCanvas();
    rc.setOnMouseEntered(e -> {
      showSizes();
    });
    ImageView imageView = getImageView(stage);

    AnchorPane rootAnchorPane = new AnchorPane();
    AnchorPane resizableCanvasAnchorPane = new AnchorPane();

    rc.widthProperty().bind(resizableCanvasAnchorPane.widthProperty());
    rc.heightProperty().bind(resizableCanvasAnchorPane.heightProperty());

    // here's where the 'magic happens'
    resizableCanvasAnchorPane.getChildren().addAll(imageView, rc);
    rootAnchorPane.getChildren().addAll(resizableCanvasAnchorPane, rc);

    createSceneAndShowStage(stage, rootAnchorPane);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
