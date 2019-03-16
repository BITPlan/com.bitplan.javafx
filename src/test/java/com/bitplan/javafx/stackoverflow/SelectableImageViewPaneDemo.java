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

import com.bitplan.javafx.SelectableImageViewPane;

import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * see
 * https://stackoverflow.com/questions/30295071/how-to-create-stackpane-on-the-drawn-rectangle-area
 * 
 * @author Roland
 *
 */
public class SelectableImageViewPaneDemo extends TestApplication {

  @Override
  public void start(Stage primaryStage) {
    ImageView imageView = super.getImageView();
    SelectableImageViewPane pane = new SelectableImageViewPane(imageView);
    pane.widthProperty().addListener((obs,oldVal,newVal)->{
      showSizes();
    });
    pane.getImageViewPane().bindSize(primaryStage);
    createSceneAndShowStage(primaryStage, pane);
  }

  public static void main(String[] args) {
    launch(args);
  }

}
