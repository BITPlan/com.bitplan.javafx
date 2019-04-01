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

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Control;

public class PercentRubberBandSelection extends RubberBandSelection {

  private RelativeSizer sizer;

  /**
   * construct me from the given sizer
   * @param sizer
   */
  public PercentRubberBandSelection(RelativeSizer sizer) {
    super(sizer.getSizer());
    this.sizer=sizer;
  }
 
  @Override
  public void addNode(Node node) {
    if (node instanceof Control) {
      Control control=(Control) node;
      Bounds pB = sizer.getSizer().getBoundsInParent();
      double rx = control.getLayoutX() / pB.getWidth();
      double ry = control.getLayoutY() / pB.getHeight();
      double rw = control.getPrefWidth() / pB.getWidth();
      double rh = control.getPrefHeight() / pB.getHeight();
      sizer.addControl(control, rx, ry, rw, rh);    
    }
    else
      super.addNode(node);
  }
  
  public void removeNode(Node node) {
    if (node instanceof Control) {
      Control control=(Control) node;
      sizer.removeControl(control);
    } else
      super.removeNode(node);
  }
 

}
