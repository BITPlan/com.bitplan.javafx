package com.bitplan.javafx.stackoverflow;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * How do I center an Image view in an anchor pane?
 * https://stackoverflow.com/a/43537764/1497139
 * @author wf
 *
 */
public class StackedImageViewTest extends TestApplication {

  @Override
  public void start(Stage primaryStage)  {
    ImageView imageView = super.getImageView(primaryStage);
    StackPane pane = new StackPane();
    pane.getChildren().add(imageView);
    StackPane.setAlignment(imageView, Pos.CENTER);
    createSceneAndShowStage(primaryStage, pane);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
