package com.bitplan.javafx.stackoverflow;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * https://stackoverflow.com/questions/16532962/how-to-make-javafx-scene-scene-resize-while-maintaining-an-aspect-ratio
 * @author wf
 *
 */
public class StageSize extends Application {
  @Override
  public void start(Stage primaryStage) {
      Button btn = new Button();
      btn.setText("Play by resizing the window");
      VBox root = new VBox();
      root.getChildren().add(btn);
      root.setStyle("-fx-background-color: gray");

      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.minWidthProperty().bind(scene.heightProperty().multiply(2));
      primaryStage.minHeightProperty().bind(scene.widthProperty().divide(2));
      primaryStage.show();
  }
  
  public static void main(String[] args) {
    launch(args);
  }
}
