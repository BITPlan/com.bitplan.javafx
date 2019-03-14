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

import static org.junit.Assert.assertEquals;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.bitplan.i18n.Translator;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

/**
 * Test selecting rectangles within an image
 * @author wf
 *
 */
public class TestSelectableImageViewPane {
 protected static Logger LOGGER = Logger.getLogger("com.bitplan.javafx");
  
  static int SHOW_TIME = 4000;
  boolean debug=false;

  private Image image;

  private ImageView imageView;
  
  @Before
  public void initGUI() {
    WaitableApp.toolkitInit();
    Translator.initialize("i18n", "en");
  }
  
  /**
   * get an imageview with a default image
   * 
   * @return - the image
   */
  public ImageView getImageView() {
    image = new Image(
        "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Dean_Franklin_-_06.04.03_Mount_Rushmore_Monument_%28by-sa%29-2_new.jpg/1280px-Dean_Franklin_-_06.04.03_Mount_Rushmore_Monument_%28by-sa%29-2_new.jpg");

    imageView = new ImageView();
    imageView.setImage(image);
    imageView.setSmooth(true);
    imageView.setCache(true);
    imageView.setPreserveRatio(true);
    return imageView;
  }
  
  class Scale {
    
    int x;
    int y;
    public Scale(int x, int y) {
      super();
      this.x = x;
      this.y = y;
    }
  }
  @Test
  public void testSelectableImageViewPane() throws InterruptedException {
    ImageView imageView = getImageView();
    SelectableImageViewPane selectPane = new SelectableImageViewPane(imageView);
    SampleApp sampleApp = new SampleApp("selectableImageViewPane", selectPane);
    sampleApp.show();
    sampleApp.waitOpen();
    int screenPercent=67;
    Rectangle2D sceneBounds=sampleApp.getSceneBounds(screenPercent,2,2);
    sampleApp.getStage().setHeight(sceneBounds.getHeight());
    sampleApp.getStage().setWidth(sceneBounds.getWidth());
    sampleApp.getStage().setX(sceneBounds.getMinX());
    sampleApp.getStage().setY(sceneBounds.getMinY());
    RubberBandSelection rbs = selectPane.getSelection();
  
    // get a rectangle with relative coordinates to the image
    Rectangle ri=selectPane.relativeToImage(0.25,0.25,0.5,0.5);
    assertEquals(ri.getWidth(),image.getWidth()/2,0.01);
    assertEquals(ri.getHeight(),image.getHeight()/2,0.01);
    Platform.runLater(()->rbs.select(ri));
    // rbs.select(0,0,selectPane.getWidth(),selectPane.getHeight());
    Scale scales[]= {new Scale(2,1), new Scale(4,3), new Scale(3,2), new Scale(16,9)};
    for (Scale scale:scales) {
     
      debug=true;
      if (debug) {
        sampleApp.getStage().setWidth(sceneBounds.getHeight()*scale.x/scale.y);
        selectPane.showBounds();
        selectPane.showBounds("bounds in local",rbs.parent.getBoundsInLocal());
      }
      selectPane.showBounds("bounds in parent",rbs.parent.getBoundsInParent());
      Thread.sleep(SHOW_TIME/scales.length);
    }
    sampleApp.close();
  }

}
