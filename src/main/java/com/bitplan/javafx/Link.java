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

import com.bitplan.gui.Linker;

import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tooltip;

/**
 * a "true" hyperlink that will call the browser
 * @author wf
 *
 */
public class Link extends Hyperlink{
  private String url;

  /**
   * create a Link
   * @param url
   * @param title
   * @param linker
   */
  public Link(String url, String title, Linker linker) {
    super(title);
    this.url=url;
    this.setTooltip(new Tooltip(url));
    setOnAction(t -> {
      linker.browse(this.url);
    });
  }
}
