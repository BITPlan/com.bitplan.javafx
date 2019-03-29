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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * see
 * http://sahits.ch/blog/blog/2012/12/30/resizable-layout-and-resizable-image/
 * http://javafx-jira.kenai.com/browse/RT-10610
 * 
 * @author akouznet
 */
public class ImageViewPaneAttempt extends StackPane {

  private ObjectProperty<ImageView> imageViewProperty = new SimpleObjectProperty<>();
  Rectangle imageBorder;
  private boolean showBorder = false;

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
  }

  /**
   * construct an empty ImageView Pane
   */
  public ImageViewPaneAttempt() {
    this(new ImageView());
  }

  public static class ImageScaler {

    private ImageView imageView;
    private Rectangle rect;
    double scaleX = 1.;
    double scaleY = 1.;
    double offsetX = 0.;
    double offsetY = 0.;
    double tx = 0.;
    double ty = 0.;
    private double fitRatio;
    private double rectRatio;

    /**
     * create an image Scaler with the given parameters
     * 
     * @param imageView
     * @param rect
     */
    public ImageScaler(ImageView imageView, Rectangle rect) {
      this.imageView = imageView;
      this.rect = rect;
    }

    /**
     * calculate the scale
     */
    public void calcScale() {
      double fitWidth = imageView.getFitWidth();
      double fitHeight = imageView.getFitHeight();
      fitRatio = fitHeight / fitWidth;
      double imgWidth = imageView.getImage().getWidth();
      double imgHeight = imageView.getImage().getHeight();
    
      double width = rect.getWidth();
      double height = rect.getHeight();

      scaleX = fitWidth / width;
      scaleY = fitHeight / height;
        
      offsetX = (fitWidth -width*scaleX) / 2.;
      offsetY = (fitHeight-height*scaleY) / 2.;
      double dx = (1. - scaleX) / 2.;
      double dy = (1. - scaleY) / 2.;
      tx = -(rect.getX() - offsetX) * scaleX - dx * imgWidth;
      ty = -(rect.getY() - offsetY) * scaleY - dy * imgHeight;
    }

    public String asString() {
      String text = String.format(
          "img: %.1f, %.1f, scale: %.1f x %.1f ofs: %.1f  %.1f, tx: %.1f x %.1f, img: %.1f rect: %.1f",
          imageView.getImage().getWidth(), imageView.getImage().getHeight(),
          scaleX, scaleY, offsetX,offsetY,tx, ty, fitRatio, rectRatio);
      return text;
    }

    /**
     * scale the rectangle to fit the image
     * 
     * @return true if there is an image and the scaling happened
     */
    public boolean scale() {
      Image image = imageView.getImage();
      if (image != null) {
        rect.setWidth(image.getWidth());
        rect.setHeight(image.getHeight());
        ImageScaler imageScaler = new ImageScaler(imageView, rect);
        // calcScale(image);
        imageScaler.calcScale();
        rect.setScaleX(scaleX);
        rect.setScaleY(scaleY);
        rect.setTranslateX(tx);
        rect.setTranslateY(ty);
        return true;
      }
      return false;
    }

  }

  @Override
  protected void layoutChildren() {
    ImageView imageView = imageViewProperty.get();
    if (imageView != null) {
      imageView.setFitWidth(getWidth());
      imageView.setFitHeight(getHeight());
      layoutInArea(imageView, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER,
          VPos.CENTER);

      if (imageBorder != null && this.showBorder) {
        ImageScaler imgScaler = new ImageScaler(imageView, imageBorder);
        if (imgScaler.scale()) {

          imageBorder.setVisible(true);

          // imageView.setVisible(false);
          // layoutInArea(imageBorder, 0, 0, getWidth(), getHeight(), 0,
          // HPos.CENTER, VPos.CENTER);
        } else {
          imageBorder.setVisible(false);
        }
      }
    }
    super.layoutChildren();
  }

  /**
   * construct the given viewPane based on the given ImageView
   * 
   * @param imageView
   */
  public ImageViewPaneAttempt(ImageView imageView) {
    initBorder();
    imageViewProperty.addListener(new ChangeListener<ImageView>() {

      @Override
      public void changed(ObservableValue<? extends ImageView> arg0,
          ImageView oldIV, ImageView newIV) {
        if (oldIV != null) {
          getChildren().remove(oldIV);
        }
        if (newIV != null) {
          getChildren().add(newIV);
        }
        if (imageBorder != null) {
          imageBorder.toFront();
        }
      }
    });
    this.imageViewProperty.set(imageView);
  }

  /**
   * initialize the Border
   */
  public void initBorder() {
    this.imageBorder = new Rectangle();
    imageBorder.setFill(Color.TRANSPARENT);
    imageBorder.setStroke(Color.BLUE);
    imageBorder.setVisible(false);
    imageBorder.setStrokeWidth(5);
    imageBorder.setStrokeType(StrokeType.INSIDE);
    this.getChildren().add(imageBorder);
  }
}
