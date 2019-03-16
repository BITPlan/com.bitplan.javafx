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
package com.bitplan.javafx.stackoverflow;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * base class for tests regarding screen sizes
 * 
 * @author wf
 *
 */
public abstract class TestApplication extends Application {
  private Image image;
  private ImageView imageView;
  protected Scene scene;
  private Stage stage;
  private Parent root;
  
  

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
  
  /**
   * get an imageview with a default image with the width and height bound to the stage
   * 
   * @param stage
   * @return - the image
   */
  public ImageView getImageView(Stage stage) {
    ImageView imageView=getImageView();
    imageView.fitWidthProperty().bind(stage.widthProperty());
    imageView.fitHeightProperty().bind(stage.heightProperty());
    return imageView;
  }

  /**
   * create the scene and show stage
   * 
   * @param stage
   * @param root
   * @return the scene created
   */
  public Scene createSceneAndShowStage(Stage stage, Parent root) {
    this.root=root;
    this.stage = stage;
    scene = new Scene(root);
    scene.setFill(Color.WHITE);
    stage.setTitle(this.getClass().getSimpleName());
    Rectangle2D s = Screen.getPrimary().getVisualBounds();
    stage.setWidth(s.getWidth() * 2 / 3);
    stage.setHeight(s.getHeight() * 2 / 3);
    stage.setScene(scene);
    if (imageView != null)
      imageView.setOnMouseClicked(event -> {
        showSizes();
      });
    stage.show();
    return scene;
  }

  /**
   * show the sizes of stage, scene, imageView and Image
   */
  public void showSizes() {
    System.out.println("Sizes: ");
    ObservableList<Screen> screens = Screen.getScreensForRectangle(stage.getX(),
        stage.getY(), stage.getWidth(), stage.getHeight());
    for (Screen screen : screens) {
      Rectangle2D s = screen.getVisualBounds();
      showSize(screen, s.getWidth(), s.getHeight());
    }
    showSize(stage, stage.getWidth(), stage.getHeight());
    showSize(scene, scene.getWidth(), scene.getHeight());
    showSize(imageView, imageView.getFitWidth(), imageView.getFitHeight());
    showSize(image, image.getWidth(), image.getHeight());
    Bounds rbip = root.getBoundsInParent();
    showSize(root,rbip.getWidth(),rbip.getHeight());
    Bounds rbil=root.getBoundsInLocal();
    showSize(root,rbil.getWidth(),rbil.getHeight());
  }

  /**
   * show the size
   * 
   * @param o
   * @param width
   * @param height
   */
  public void showSize(Object o, double width, double height) {
    System.out.println(String.format("%25s %4.0f x %4.0f",
        o.getClass().getSimpleName(), width, height));
  }

}
