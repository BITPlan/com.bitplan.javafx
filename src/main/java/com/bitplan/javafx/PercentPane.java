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

import javafx.scene.layout.Pane;
import java.util.ArrayList;
import javafx.beans.Observable;
import javafx.scene.Parent;
import javafx.scene.control.Control;

/**
 * We want a Pane that will automatically resize its components The components
 * width height and position are all expressed in percentages of the width and
 * height of the Pane as the Pane resizes the position and sizes will all remain
 * relative aspect ratio is not respected
 * 
 * see https://gist.github.com/chriscamacho/4f8b2e3e8f8340278b7c
 * 
 * @author chriscamacho
 *
 */
public class PercentPane extends Pane implements PercentSizer {

  /**
   * helper class
   */
  public class ControlBundle {
    public double x, y, w, h;
    public Control control;

    ControlBundle(Control c, double X, double Y, double W, double H) {
      control = c;
      x = X;
      y = Y;
      w = W;
      h = H;
    }
  }

  ArrayList<ControlBundle> controls = new ArrayList<ControlBundle>();

  PercentPane() {
    super();
    widthProperty().addListener(o -> sizeListener(o));
    heightProperty().addListener(o -> sizeListener(o));
  }

  void sizeListener(Observable o) {
    for (ControlBundle cb : controls) {

      double newWidth = getWidth() * (cb.w / 100.0);
      double newHeight = getHeight() * (cb.h / 100.0);

      cb.control.setPrefWidth(newWidth);
      cb.control.setMinWidth(newWidth);
      cb.control.setMaxWidth(newWidth);

      cb.control.setPrefHeight(newHeight);
      cb.control.setMinHeight(newHeight);
      cb.control.setMaxHeight(newHeight);

      cb.control.setTranslateX(getWidth() * (cb.x / 100.0));
      cb.control.setTranslateY(getHeight() * (cb.y / 100.0));

    }

  }

  /**
   * add a control with the given percentages
   * @param ctrl
   * @param x
   * @param y
   * @param w
   * @param h
   */
  public void addControl(Control ctrl, double x, double y, double w, double h) {
    this.getChildren().add(ctrl);
    controls.add(new ControlBundle(ctrl, x, y, w, h));
  }

  @Override
  public Parent getSizer() {
    return this;
  }

}
