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
package com.bitplan.javafx;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * allows showing borders
 * @author wf
 *
 */
public class BorderPane extends RelativePane {
  public static boolean debug;
  
  private Line line1, line2;
  private Rectangle rect;

  /**
   * add a line
   * @return the line to be added
   */
  public Line addLine() {
    Line line = new Line();
    line.setStroke(Color.RED);
    line.setStrokeWidth(2);
    this.getChildren().add(line);
    return line;
  }

  /**
   * construct me
   */
  public BorderPane() {
    if (debug) {
      line1 = addLine();
      line1.setStartX(0.);
      line1.setStartY(0.);
      line1.endXProperty().bind(this.widthProperty());
      line1.endYProperty().bind(this.heightProperty());
      line2 = addLine();
      line2.startXProperty().bind(this.widthProperty());
      line2.setStartY(0.);
      line2.setEndX(0.);
      line2.endYProperty().bind(this.heightProperty());
    }
    rect=new Rectangle();
    rect.setX(0.);
    rect.setY(0.);
    rect.widthProperty().bind(this.widthProperty());
    rect.heightProperty().bind(this.heightProperty());
    rect.setFill(Color.TRANSPARENT);
    rect.setStroke(Color.BLUE);
    rect.setVisible(true);
    rect.setStrokeWidth(4);
    rect.setStrokeType(StrokeType.CENTERED);
    this.getChildren().add(rect);
  }
}