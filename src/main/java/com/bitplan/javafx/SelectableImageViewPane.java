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

import com.bitplan.javafx.RubberBandSelection.Selection;

import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * an ImageViewPane with a RubberBandSelection that properly resizes the
 * rectangles
 * 
 * @author wf
 *
 */
public class SelectableImageViewPane extends StackPane {
  protected static Logger LOGGER = Logger.getLogger("com.bitplan.javafx");
  public static boolean debug = true;
  private RubberBandSelection selection;
  private ImageViewPane imageViewPane;
  private Pane glassPane;

  /**
   * get my selection
   * 
   * @return
   */
  public RubberBandSelection getSelection() {
    return selection;
  }

  /**
   * show the bound of the given node with the given title
   * 
   * @param title
   * @param n
   */
  public void showBounds(String title, Node n) {
    if (debug) {
      showBounds(title, n.getLayoutBounds());
    }
  }

  /**
   * show the bound of the given node with the given title
   * 
   * @param title
   * @param b
   */
  public void showBounds(String title, Bounds b) {
    LOGGER.log(Level.INFO, String.format("%s: min %.0f,%.0f max %.0f,%.0f",
        title, b.getMinX(), b.getMinY(), b.getMaxX(), b.getMaxY()));
  }

  /**
   * show my bounds
   */
  public void showBounds() {
    if (debug) {
      showBounds("pane", this);
      showBounds("imageViewPane", getImageViewPane());
      showBounds("glassPane", glassPane);
      showBounds("imageView", getImageViewPane().imageViewProperty().get());
    }
  }
  
  /**
   * @return the imageView
   * 
   */
  public ImageView getImageView() {
    return getImageViewPane().getImageView();
  }
  
  public ImageViewPane getImageViewPane() {
    return imageViewPane;
  }

  public void setImageViewPane(ImageViewPane imageViewPane) {
    this.imageViewPane = imageViewPane;
  }

  /**
   * get a rectangle relative to the image with the given relative coordinates
   * @param rx
   * @param ry
   * @param rw
   * @param rh
   * @return the rectangle
   */
  public Rectangle relativeToImage(double rx, double ry, double rw, double rh) {
    ImageView imageView = getImageView();
    double iw = imageView.getImage().getWidth();
    double ih = imageView.getImage().getHeight();
    Rectangle rect = new Rectangle(iw * rw, ih * rh);
    rect.setX(rx*iw);
    rect.setY(ry*ih);
    return rect;
  }
  
  /**
   * get a rectangle relative to the given image based on the given bounds
   * @param r
   * @return
   */
  public Rectangle relativeToImage(Bounds b) {
    Rectangle rect=this.relativeToImage(b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight());
    return rect;
  }

  @Override
  protected void layoutChildren() {
    super.layoutChildren();
    showBounds();
    int index = 1;
    for (Selection s : selection.selected.values()) {

      if (debug) {
        showBounds("" + (index++), s.node);
        LOGGER.log(Level.INFO, s.asPercent());
      }
      Bounds rB = s.relativeBounds;
      double w = getWidth();
      double h = getHeight();
      double minX = rB.getMinX() * w;
      double minY = rB.getMinY() * h;
      double width = rB.getWidth() * w;
      double height = rB.getHeight() * h;
      layoutInArea(s.node, minX, minY, width, height, 0, HPos.LEFT, VPos.TOP);
    }
  }

  /**
   * create me
   * 
   * @param imageView
   */
  public SelectableImageViewPane(ImageView imageView) {
    setImageViewPane(new ImageViewPane(imageView));
    getImageViewPane().setShowBorder(true);
    getChildren().add(getImageViewPane());
    StackPane.setAlignment(getImageViewPane(), Pos.CENTER);
    glassPane = new Pane();
    glassPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.1);");
    getChildren().add(glassPane);
    //glassPane.prefWidthProperty().bind(imageView.fitWidthProperty());
    // glassPane.prefHeightProperty().bind(imageView.fitHeightProperty());
    selection = new RubberBandSelection(glassPane);
    selection.setSelectButton(true);
  }

}
