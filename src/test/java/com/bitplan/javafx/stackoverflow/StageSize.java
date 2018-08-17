package com.bitplan.javafx.stackoverflow;

import javafx.stage.Stage;

/**
 * https://stackoverflow.com/questions/16532962/how-to-make-javafx-scene-scene-resize-while-maintaining-an-aspect-ratio
 * 
 * @author wf
 *
 */
public class StageSize extends StackedImageViewTest {
  @Override
  public void start(Stage primaryStage) {
    super.start(primaryStage);
    primaryStage.minWidthProperty().bind(scene.heightProperty().multiply(2));
    primaryStage.minHeightProperty().bind(scene.widthProperty().divide(2));
  }

  public static void main(String[] args) {
    launch(args);
  }
}
