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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

/**
 * a resizable Pane that shows an image
 * 
 * @author wf
 *
 */
public class ImageViewPane extends AnchorPane {

  public static boolean debug=false;
  private ObjectProperty<ImageView> imageViewProperty = new SimpleObjectProperty<>();
  Border imageBorder;
  private boolean showBorder = false;
  private ObservableDoubleValue bindWidthProperty;
  private ObservableDoubleValue bindHeightProperty;

  public ObjectProperty<ImageView> imageViewProperty() {
    return imageViewProperty;
  }

  public ImageView getImageView() {
    return imageViewProperty.get();
  }

  public void setImageView(ImageView imageView) {
    this.imageViewProperty.set(imageView);
  }

  public boolean isShowBorder() {
    return showBorder;
  }

  public void setShowBorder(boolean showBorder) {
    this.showBorder = showBorder;
    imageBorder.setVisible(showBorder);
  }

  /**
   * create an imageView Pane for the given ImageView
   * 
   * @param imageView
   */
  public ImageViewPane(ImageView imageView) {
    this.setImageView(imageView);
    imageView.setSmooth(true);
    imageView.setCache(true);
    imageView.setPreserveRatio(true);
    getChildren().add(imageView);
    StackPane.setAlignment(imageView, Pos.CENTER);
    initBorder();
    imageViewProperty.addListener((obs, oldIV, newIV) -> {
      if (oldIV != null) {
        getChildren().remove(oldIV);
      }
      if (newIV != null) {
        getChildren().add(newIV);
        bindSize();
      }
      if (imageBorder != null) {
        imageBorder.toFront();
      }
    });
  }

  public static class Border extends Pane {

    private Line line1, line2;
    private Rectangle rect;

    public Line addLine() {
      Line line = new Line();
      line.setStroke(Color.RED);
      line.setStrokeWidth(2);
      this.getChildren().add(line);
      return line;
    }

    public Border() {
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
      rect.widthProperty().bind(this.widthProperty());
      rect.heightProperty().bind(this.heightProperty());
      rect.setFill(Color.TRANSPARENT);
      rect.setStroke(Color.BLUE);
      rect.setVisible(false);
      rect.setStrokeWidth(3);
      rect.setStrokeType(StrokeType.CENTERED);
      this.getChildren().add(rect);
    }
  }

  /**
   * initialize the Border
   */
  public void initBorder() {
    this.imageBorder = new Border();
    this.getChildren().add(imageBorder);
  }

  /**
   * bind my size with the given properties
   * 
   * @param widthProperty
   * @param heightProperty
   */
  public void bindSize(ObservableDoubleValue widthProperty,
      ObservableDoubleValue heightProperty) {
    this.bindWidthProperty = widthProperty;
    this.bindHeightProperty = heightProperty;
    bindSize();
  }

  /**
   * bind my Sizes
   */
  public void bindSize() {
    if (bindWidthProperty != null && bindHeightProperty != null) {
      getImageView().fitWidthProperty().bind(bindWidthProperty);
      getImageView().fitHeightProperty().bind(bindHeightProperty);
      imageBorder.prefWidthProperty().bind(getImageView().fitWidthProperty());
      imageBorder.prefHeightProperty().bind(getImageView().fitHeightProperty());
    }
  }

  /**
   * bind my size to the stage's size
   * 
   * @param stage
   */
  public void bindSize(Stage stage) {
    bindSize(stage.widthProperty(), stage.heightProperty());
  }
}
