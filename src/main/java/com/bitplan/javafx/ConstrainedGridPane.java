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
package com.bitplan.javafx;

import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;

/**
 * a GridPane with Column and RowConstraints
 * @author wf
 *
 */
public class ConstrainedGridPane extends GridPane {
  /**
   * helper function for images
   * @param icon
   * @return the BoderPane that wraps the given icon
   */
  public BorderPane wrapImageView(ImageView icon) {
    BorderPane pane = new BorderPane();
    pane.setCenter(icon);
    // resize images automatically
    // https://stackoverflow.com/a/12635224/1497139
    icon.setPreserveRatio(true);
    icon.fitHeightProperty().bind(pane.heightProperty());
    icon.fitWidthProperty().bind(pane.widthProperty());
    return pane;
  }
  
  /**
   * fix the columnSizes to the given column Width
   * @param colWidths
   */
  public void fixColumnSizes(int hGap,int... colWidths) {
    this.setHgap(hGap);
    // Setting columns size in percent
    for (int colWidth : colWidths) {
      ColumnConstraints column = new ColumnConstraints();
      column.setPercentWidth(colWidth);
      getColumnConstraints().add(column);
    }
  }

  /**
   * fix the rowSizes to the given rowHeights
   * @param rowHeights
   */
  public void fixRowSizes(int vGap,int... rowHeights) {
    this.setVgap(vGap);
    for (int rowHeight : rowHeights) {
      RowConstraints rowc = new RowConstraints();
      rowc.setPercentHeight(rowHeight);
      getRowConstraints().add(rowc);
    }

    // grid.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT); // Default width and
    // height
    this.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
  }
}
