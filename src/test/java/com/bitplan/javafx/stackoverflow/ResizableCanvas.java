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

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
/**
 * javafx - access height and width values of an image after it is resized to preserve aspect ratio
 * https://stackoverflow.com/a/36291104/1497139
 * @author wf
 *
 */
public class ResizableCanvas extends Canvas {

  /**
   * construct me
   */
  public ResizableCanvas() {
    // Redraw canvas when size changes.
    widthProperty().addListener(evt -> draw());
    heightProperty().addListener(evt -> draw());
  }

  private void draw() {
    double width = getWidth();
    double height = getHeight();
    GraphicsContext gc = getGraphicsContext2D();
    gc.clearRect(0, 0, width, height);
    gc.setStroke(Color.RED);
    gc.strokeLine(0, 0, width, height);
    gc.strokeLine(0, height, width, 0);
  }

  @Override
  public boolean isResizable() {
    return true;
  }

  @Override
  public double prefWidth(double height) {
    return getWidth();
  }

  @Override
  public double prefHeight(double width) {
    return getHeight();
  }

}
