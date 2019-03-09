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

import com.bitplan.javafx.ImageViewPane;
import com.bitplan.javafx.RubberBandSelection;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * see
 * https://stackoverflow.com/questions/30295071/how-to-create-stackpane-on-the-drawn-rectangle-area
 * 
 * @author Roland
 *
 */
public class RubberBandSelectionDemo extends TestApplication {

  CheckBox drawButtonCheckBox;

  @Override
  public void start(Stage primaryStage) {
    ImageView imageView = super.getImageView();
    ImageViewPane pane = new ImageViewPane(imageView);
    RubberBandSelection rbs = new RubberBandSelection(pane);

    drawButtonCheckBox = new CheckBox("Draw Button");
    drawButtonCheckBox.selectedProperty()
        .addListener(new ChangeListener<Boolean>() {
          @Override
          public void changed(ObservableValue<? extends Boolean> observable,
              Boolean oldValue, Boolean newValue) {
            rbs.setSelectButton(newValue);
          }
        });

    pane.getChildren().add(drawButtonCheckBox);
    createSceneAndShowStage(primaryStage, pane);
  }

  public static void main(String[] args) {
    launch(args);
  }

}
