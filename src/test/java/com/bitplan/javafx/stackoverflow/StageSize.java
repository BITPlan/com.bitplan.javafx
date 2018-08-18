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

import javafx.stage.Stage;

/**
 * https://stackoverflow.com/questions/16532962/how-to-make-javafx-scene-scene-resize-while-maintaining-an-aspect-ratio
 * 
 * @author wf
 *
 */
public class StageSize extends StackedImageViewTest {
  @Override
  public void start(Stage primaryStage) {
    super.start(primaryStage);
    primaryStage.minWidthProperty().bind(scene.heightProperty().multiply(2));
    primaryStage.minHeightProperty().bind(scene.widthProperty().divide(2));
  }

  public static void main(String[] args) {
    launch(args);
  }
}
