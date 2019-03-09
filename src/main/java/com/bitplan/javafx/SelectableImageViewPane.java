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

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * an ImageViewPane with a RubberBandSelection that properly resizes the rectangles
 * @author wf
 *
 */
public class SelectableImageViewPane extends StackPane {
  protected static Logger LOGGER = Logger.getLogger("com.bitplan.javafx");
  public static boolean debug=true;
  private RubberBandSelection selection;
  private ImageViewPane imageViewPane;
  private Pane glassPane;
  
  /**
   * get my selection
   * @return
   */
  public RubberBandSelection getSelection() {
    return selection;
  }
  
  /**
   * show the bound of the given node with the given title
   * @param title
   * @param n
   */
  public void showBounds(String title,Node n) {
    if (debug) {
      Bounds b = n.getLayoutBounds();
      LOGGER.log(Level.INFO,String.format("%s: min %.0f,%.0f max %.0f,%.0f",title,b.getMinX(),b.getMinY(),b.getMaxX(),b.getMaxY()));        
    }
  }
  
  @Override
  protected void layoutChildren() {
    super.layoutChildren();
    int index=1;
    if (debug) {
      showBounds("pane",this);
      showBounds("imageViewPane",imageViewPane);
      showBounds("glassPane",glassPane);
      showBounds("imageView",imageViewPane.imageViewProperty().get());
    }
    for (Node n:selection.selected) {
     showBounds(""+(index++),n);
    }
  }

  /**
   * create me
   * @param imageView
   */
  public SelectableImageViewPane(ImageView imageView) {
    imageViewPane=new ImageViewPane(imageView);
    getChildren().add(imageViewPane);
    StackPane.setAlignment(imageViewPane, Pos.CENTER);
    glassPane = new AnchorPane();
    glassPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.1);");
    getChildren().add(glassPane);
    //glassPane.prefWidthProperty().bind(imageViewPane.widthProperty());
    //glassPane.prefHeightProperty().bind(imageViewPane.heightProperty());
    selection = new RubberBandSelection(glassPane);
    selection.setSelectButton(true);
  }

}
