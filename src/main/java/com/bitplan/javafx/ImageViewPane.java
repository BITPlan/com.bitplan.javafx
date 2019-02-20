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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

/**
 * see
 * http://sahits.ch/blog/blog/2012/12/30/resizable-layout-and-resizable-image/
 * http://javafx-jira.kenai.com/browse/RT-10610
 * 
 * @author akouznet
 */
public class ImageViewPane extends Region {

  private ObjectProperty<ImageView> imageViewProperty = new SimpleObjectProperty<>();

  public ObjectProperty<ImageView> imageViewProperty() {
    return imageViewProperty;
  }

  public ImageView getImageView() {
    return imageViewProperty.get();
  }

  public void setImageView(ImageView imageView) {
    this.imageViewProperty.set(imageView);
  }

  public ImageViewPane() {
    this(new ImageView());
  }

  @Override
  protected void layoutChildren() {
    ImageView imageView = imageViewProperty.get();
    if (imageView != null) {
      imageView.setFitWidth(getWidth());
      imageView.setFitHeight(getHeight());
      layoutInArea(imageView, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER,
          VPos.CENTER);
    }
    super.layoutChildren();
  }

  /**
   * construct the given viewPane based on the given ImageView
   * @param imageView
   */
  public ImageViewPane(ImageView imageView) {
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
      }
    });
    this.imageViewProperty.set(imageView);
  }
}
