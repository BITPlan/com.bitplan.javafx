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

import javafx.scene.Parent;
import javafx.scene.control.Control;

/**
 * allow adding controls with x,y width and heigh given in percent of the parent
 * @author wf
 *
 */
public interface PercentSizer {
  /**
   * add a control with the given percentages
   * @param ctrl
   * @param x
   * @param y
   * @param w
   * @param h
   */
  public void addControl(Control ctrl, double x, double y, double w, double h);
  
  /**
   * return the parent doing the size handling
   * @return
   */
  public Parent getSizer();
}
