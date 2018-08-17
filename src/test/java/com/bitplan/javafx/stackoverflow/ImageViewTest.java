package com.bitplan.javafx.stackoverflow;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * javafx - access height and width values of an image after it is resized to
 * preserve aspect ratio https://stackoverflow.com/a/36291104/1497139
 * 
 * @author wf
 *
 */
public class ImageViewTest extends TestApplication {

  @Override
  public void start(Stage stage) throws Exception {

    ResizableCanvas rc = new ResizableCanvas();
    rc.setOnMouseEntered(e -> {
      showSizes();
    });
    ImageView imageView = getImageView(stage);

    AnchorPane rootAnchorPane = new AnchorPane();
    AnchorPane resizableCanvasAnchorPane = new AnchorPane();

    rc.widthProperty().bind(resizableCanvasAnchorPane.widthProperty());
    rc.heightProperty().bind(resizableCanvasAnchorPane.heightProperty());

    // here's where the 'magic happens'
    resizableCanvasAnchorPane.getChildren().addAll(imageView, rc);
    rootAnchorPane.getChildren().addAll(resizableCanvasAnchorPane, rc);

    createSceneAndShowStage(stage, rootAnchorPane);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
